package com.example.ars.features.escaner

import com.example.ars.ProgresoEnMemoria
import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.domain.usecase.RegistrarObraUseCase
import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.usecase.ElegirCriaturaInicialUseCase
import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase
import com.example.ars.features.museos.domain.usecase.RegistrarVisitaMuseoUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrarObraUseCaseTest {
    private val museos = MuseosRepositoryImpl()
    private val catalogo = CatalogoRepositoryImpl(museos)
    private val criaturas = HomeRepositoryImpl()

    @Test
    fun unaObraDesbloqueaSuMuseoSinConcederLaCriaturaDeLaObra() = runBlocking {
        val progreso = ProgresoEnMemoria(ProgresoUsuario("criatura_012", listOf("criatura_012")))
        val museo = museos.obtenerMuseos().first()
        val obra = catalogo.obtenerObras()[1]
        val registrar = RegistrarObraUseCase(progreso, criaturas)
        val primera = registrar(obra, museo).getOrThrow()
        assertFalse(primera.yaEscaneada)
        assertEquals(listOf(obra.id), progreso.progreso.obrasEscaneadas)
        assertEquals(listOf(museo.id), progreso.progreso.museosVisitados)
        assertEquals(listOf("criatura_001", "criatura_012"), progreso.progreso.criaturasDescubiertas)
        assertEquals("criatura_001", primera.criaturaBienvenidaId)
        val repetida = registrar(obra, museo).getOrThrow()
        assertTrue(repetida.yaEscaneada)
        assertNull(repetida.criaturaBienvenidaId)
        assertEquals(1, progreso.guardados)
    }

    @Test
    fun unMuseoYaVisitadoConservaSuBienvenidaYElStarter() = runBlocking {
        val anterior = ProgresoUsuario("criatura_001", listOf("criatura_001"), listOf("museo_001"))
        val progreso = ProgresoEnMemoria(anterior)
        val obra = catalogo.obtenerObras().first()
        val resultado = RegistrarObraUseCase(progreso, criaturas)(obra, museos.obtenerMuseos().first()).getOrThrow()
        assertNull(resultado.criaturaBienvenidaId)
        assertEquals(anterior.criaturasDescubiertas, progreso.progreso.criaturasDescubiertas)
        assertEquals(anterior.museosVisitados, progreso.progreso.museosVisitados)
    }

    @Test
    fun unFalloDeGuardadoNoDejaVisitaNiObraAMedias() = runBlocking {
        val progreso = ProgresoEnMemoria().apply { fallaGuardado = true }
        val registrar = RegistrarObraUseCase(progreso, criaturas)
        val museo = museos.obtenerMuseos().first()
        val obra = catalogo.obtenerObras().first()
        assertTrue(registrar(obra, museo).isFailure)
        assertEquals(ProgresoUsuario(), progreso.progreso)
        progreso.fallaGuardado = false
        assertFalse(registrar(obra, museo).getOrThrow().yaEscaneada)
        assertEquals(1, progreso.guardados)
    }

    @Test
    fun rechazaUnaAsociacionIncorrectaAntesDeGuardar() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val resultado = RegistrarObraUseCase(progreso, criaturas)(
            catalogo.obtenerObras().first(), museos.obtenerMuseos()[1]
        )
        assertTrue(resultado.isFailure)
        assertEquals(0, progreso.guardados)
    }

    @Test
    fun lecturasSimultaneasConservanObrasVisitaYStarterSinDuplicarlos() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val registrar = RegistrarObraUseCase(progreso, criaturas)
        val museo = museos.obtenerMuseos().first()
        val obras = catalogo.obtenerObras()
        coroutineScope {
            listOf(
                async { registrar(obras[0], museo).getOrThrow() },
                async { registrar(obras[1], museo).getOrThrow() },
                async { registrar(obras[0], museo).getOrThrow() },
                async { RegistrarVisitaMuseoUseCase(progreso, criaturas)(museo).getOrThrow() },
                async { ElegirCriaturaInicialUseCase(progreso)("criatura_012").getOrThrow() }
            ).awaitAll()
        }
        assertEquals(setOf("obra_001", "obra_002"), progreso.progreso.obrasEscaneadas.toSet())
        assertEquals(2, progreso.progreso.obrasEscaneadas.size)
        assertEquals(listOf("museo_001"), progreso.progreso.museosVisitados)
        assertEquals(setOf("criatura_001", "criatura_012"), progreso.progreso.criaturasDescubiertas.toSet())
    }

    @Test
    fun calculaContadoresDesdeElCatalogoYLosIdsUnicos() = runBlocking {
        val progreso = ProgresoEnMemoria(ProgresoUsuario(
            criaturasDescubiertas = listOf("criatura_001", "criatura_001"),
            obrasEscaneadas = listOf("obra_001", "obra_001", "desconocida")
        ))
        val lista = ObtenerCatalogoMuseosUseCase(museos, catalogo, progreso)().getOrThrow()
        val prado = lista.first()
        assertEquals(2, prado.obrasTotales)
        assertEquals(1, prado.obrasDescubiertas)
        assertEquals(3, prado.criaturasTotales)
        assertEquals(1, prado.criaturasDescubiertas)
        assertEquals(1, lista[1].obrasTotales)
        assertEquals(0, lista[2].obrasTotales)
    }

    @Test
    fun leeElProgresoAnteriorSinPerderElStarterNiLasVisitas() {
        val anterior = """{"criaturaInicialId":"criatura_012","criaturasDescubiertas":["criatura_012"],"museosVisitados":["museo_002"]}"""
        val progreso = Json.decodeFromString<ProgresoUsuario>(anterior)
        assertEquals("criatura_012", progreso.criaturaInicialId)
        assertEquals(listOf("museo_002"), progreso.museosVisitados)
        assertTrue(progreso.obrasEscaneadas.isEmpty())
    }
}
