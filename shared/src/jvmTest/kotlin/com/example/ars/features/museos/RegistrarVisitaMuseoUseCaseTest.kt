package com.example.ars.features.museos

import com.example.ars.ProgresoEnMemoria
import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.usecase.ElegirCriaturaInicialUseCase
import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.domain.usecase.RegistrarVisitaMuseoUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrarVisitaMuseoUseCaseTest {
    @Test
    fun visitaYBienvenidaSeGuardanJuntasUnaSolaVez() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val museo = MuseosRepositoryImpl().obtenerMuseos().first()
        val registrar = RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))
        val primera = registrar(museo).getOrThrow()
        assertFalse(primera.yaVisitado)
        assertEquals("criatura_001", primera.criaturaObtenidaId)
        assertEquals(listOf(museo.id), progreso.progreso.museosVisitados)
        assertEquals(listOf("criatura_001"), progreso.progreso.criaturasDescubiertas)
        val repetida = registrar(museo).getOrThrow()
        assertTrue(repetida.yaVisitado)
        assertNull(repetida.criaturaObtenidaId)
        assertEquals(1, progreso.guardados)
    }

    @Test
    fun respetaElStarterYLosDatosAnteriores() = runBlocking {
        val anterior = ProgresoUsuario("criatura_001", listOf("criatura_001", "criatura_012"), listOf("museo_002"))
        val progreso = ProgresoEnMemoria(anterior)
        val museo = MuseosRepositoryImpl().obtenerMuseos().first()
        val resultado = RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))(museo).getOrThrow()
        assertNull(resultado.criaturaObtenidaId)
        assertEquals(anterior.criaturasDescubiertas, progreso.progreso.criaturasDescubiertas)
        assertEquals(anterior.criaturaInicialId, progreso.progreso.criaturaInicialId)
        assertEquals(listOf("museo_002", "museo_001"), progreso.progreso.museosVisitados)
    }

    @Test
    fun museoSinBienvenidaSoloRegistraLaVisita() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val museo = MuseosRepositoryImpl().obtenerMuseos()[1]
        RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))(museo).getOrThrow()
        assertEquals(listOf(museo.id), progreso.progreso.museosVisitados)
        assertTrue(progreso.progreso.criaturasDescubiertas.isEmpty())
    }

    @Test
    fun configuracionIncorrectaNoRegistraUnaVisitaParcial() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val museo = MuseosRepositoryImpl().obtenerMuseos().first()
            .copy(criaturaBienvenidaId = "inexistente")
        val resultado = RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))(museo)
        assertTrue(resultado.isFailure)
        assertEquals(0, progreso.guardados)
        assertTrue(progreso.progreso.museosVisitados.isEmpty())
    }

    @Test
    fun elFalloDeGuardadoPermiteReintentarLaBienvenida() = runBlocking {
        val progreso = ProgresoEnMemoria().apply { fallaGuardado = true }
        val museo = MuseosRepositoryImpl().obtenerMuseos().first()
        val registrar = RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))
        assertTrue(registrar(museo).isFailure)
        assertTrue(progreso.progreso.museosVisitados.isEmpty())
        progreso.fallaGuardado = false
        assertEquals("criatura_001", registrar(museo).getOrThrow().criaturaObtenidaId)
        assertEquals(1, progreso.guardados)
    }

    @Test
    fun dosVisitasYElStarterConservanLosTresCambios() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val museos = MuseosRepositoryImpl().obtenerMuseos()
        val registrar = RegistrarVisitaMuseoUseCase(progreso, HomeRepositoryImpl(), CatalogoRepositoryImpl(MuseosRepositoryImpl()))
        coroutineScope {
            listOf(
                async { registrar(museos[0]).getOrThrow() },
                async { registrar(museos[1]).getOrThrow() },
                async { ElegirCriaturaInicialUseCase(progreso)("criatura_012").getOrThrow() }
            ).awaitAll()
        }
        assertEquals(setOf("museo_001", "museo_002"), progreso.progreso.museosVisitados.toSet())
        assertEquals(setOf("criatura_001", "criatura_012"), progreso.progreso.criaturasDescubiertas.toSet())
        assertEquals("criatura_012", progreso.progreso.criaturaInicialId)
    }
}
