package com.example.ars.features.museos

import com.example.ars.ProgresoEnMemoria
import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.usecase.RegistrarObraUseCase
import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.domain.model.Mision
import com.example.ars.features.museos.domain.model.ObjetivoMision
import com.example.ars.features.museos.domain.usecase.EvaluarMisionesUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MisionesUseCaseTest {
    private val museosRepository = MuseosRepositoryImpl()
    private val catalogo = CatalogoRepositoryImpl(museosRepository)
    private val evaluar = EvaluarMisionesUseCase()

    @Test
    fun laCantidadCuentaObrasDistintasDelMuseo() = runBlocking {
        val museo = museosRepository.obtenerMuseos().first()
        val progreso = ProgresoUsuario(
            museosVisitados = listOf(museo.id),
            obrasEscaneadas = listOf("obra_001", "obra_001", "obra_003", "inexistente")
        )
        val resultado = evaluar(museo, catalogo.obtenerObras(), progreso)
        val cantidad = resultado.misiones.first { it.mision.id == "prado_cantidad" }
        assertEquals(1, cantidad.actual)
        assertFalse(cantidad.completada)
        assertTrue(resultado.progreso.criaturasDescubiertas.isEmpty())
    }

    @Test
    fun elGrupoExigeLasCuatroObrasConcretas() = runBlocking {
        val museo = museosRepository.obtenerMuseos().first().copy(
            misiones = listOf(Mision(
                "grupo", "Cuatro obras", ObjetivoMision.ObrasConcretas(listOf("a", "b", "c", "d")),
                "criatura_004"
            ))
        )
        val obras = listOf("a", "b", "c", "d", "otra").map {
            Obra(it, it, "Autor", museo.id, "")
        }
        val parcial = ProgresoUsuario(
            museosVisitados = listOf(museo.id), obrasEscaneadas = listOf("a", "b", "c", "otra")
        )
        assertFalse(evaluar(museo, obras, parcial).misiones.single().completada)
        val completo = evaluar(museo, obras, parcial.copy(obrasEscaneadas = listOf("a", "b", "c", "d")))
        assertTrue(completo.misiones.single().completada)
        assertEquals(listOf("criatura_004"), completo.progreso.criaturasDescubiertas)
        assertEquals(listOf("grupo"), completo.progreso.misionesCompletadas)
    }

    @Test
    fun laRecompensaConjuntaEsperaLasDosMisiones() = runBlocking {
        val museo = museosRepository.obtenerMuseos()[2]
        val obras = listOf(
            Obra("obra_thyssen_caballero", "Joven caballero en un paisaje", "Vittore Carpaccio", museo.id, ""),
            Obra("obra_thyssen_otra", "Otra obra de prueba", "Autor", museo.id, "")
        )
        val parcial = ProgresoUsuario(
            museosVisitados = listOf(museo.id), obrasEscaneadas = listOf("obra_thyssen_caballero")
        )
        val primero = evaluar(museo, obras, parcial)
        assertTrue(primero.progreso.criaturasDescubiertas.isEmpty())
        assertEquals(listOf("thyssen_caballero"), primero.progreso.misionesCompletadas)
        val segundo = evaluar(museo, obras, primero.progreso.copy(
            obrasEscaneadas = listOf("obra_thyssen_caballero", "obra_thyssen_otra")
        ))
        assertEquals(listOf("criatura_015"), segundo.progreso.criaturasDescubiertas)
        assertEquals(segundo.progreso, evaluar(museo, obras, segundo.progreso).progreso)
    }

    @Test
    fun elStarterOcultaSuMisionSinOcultarLasOtras() = runBlocking {
        val museo = museosRepository.obtenerMuseos()[1]
        val elegido = evaluar(museo, catalogo.obtenerObras(), ProgresoUsuario(
            criaturaInicialId = "criatura_002", criaturasDescubiertas = listOf("criatura_002"),
            museosVisitados = listOf(museo.id)
        ))
        assertEquals(listOf("reina_cantidad"), elegido.misiones.map { it.mision.id })
        val otro = evaluar(museo, catalogo.obtenerObras(), ProgresoUsuario(criaturaInicialId = "criatura_012"))
        assertEquals(2, otro.misiones.size)
    }

    @Test
    fun losStartersAntiguosTambienOmitenUnaRecompensaConjuntaYaElegida() = runBlocking {
        val museo = museosRepository.obtenerMuseos()[4]
        val resultado = evaluar(museo, catalogo.obtenerObras(), ProgresoUsuario(
            criaturaInicialId = "criatura_005", criaturasDescubiertas = listOf("criatura_005"),
            museosVisitados = listOf(museo.id)
        ))
        assertTrue(resultado.misiones.isEmpty())
        assertEquals(listOf("criatura_005"), resultado.progreso.criaturasDescubiertas)
    }

    @Test
    fun lasMisionesSinCatalogoQuedanPendientes() = runBlocking {
        val museo = museosRepository.obtenerMuseos()[4]
        val resultado = evaluar(museo, emptyList(), ProgresoUsuario(
            museosVisitados = listOf(museo.id), obrasEscaneadas = listOf("obra_vangogh_girasoles")
        ))
        assertTrue(resultado.misiones.none { it.disponible || it.completada })
        assertTrue(resultado.progreso.criaturasDescubiertas.isEmpty())
    }

    @Test
    fun actualizaLasObrasAntiguasYNoRepiteRecompensasAlCargar() = runBlocking {
        val progreso = ProgresoEnMemoria(ProgresoUsuario(
            criaturaInicialId = "criatura_012", criaturasDescubiertas = listOf("criatura_001", "criatura_012"),
            museosVisitados = listOf("museo_001"), obrasEscaneadas = listOf("obra_001", "obra_002")
        ))
        val obtener = ObtenerProgresoUseCase(progreso, museosRepository, catalogo)
        val primero = obtener().getOrThrow()
        assertTrue(primero.misionesCompletadas.containsAll(listOf("prado_cantidad", "prado_jardin")))
        assertEquals(setOf("criatura_001", "criatura_004", "criatura_008", "criatura_012"),
            primero.criaturasDescubiertas.toSet())
        assertEquals(primero, obtener().getOrThrow())
        assertEquals(1, progreso.guardados)
    }

    @Test
    fun falloDeGuardadoConservaObrasMisionesYRecompensasParaReintentar() = runBlocking {
        val anterior = ProgresoUsuario(
            criaturasDescubiertas = listOf("criatura_001"), museosVisitados = listOf("museo_001"),
            obrasEscaneadas = listOf("obra_001")
        )
        val progreso = ProgresoEnMemoria(anterior).apply { fallaGuardado = true }
        val registrar = RegistrarObraUseCase(progreso, HomeRepositoryImpl(), catalogo)
        val museo = museosRepository.obtenerMuseos().first()
        val jardin = catalogo.obtenerObras()[1]
        assertTrue(registrar(jardin, museo).isFailure)
        assertEquals(anterior, progreso.progreso)
        progreso.fallaGuardado = false
        val resultado = registrar(jardin, museo).getOrThrow()
        assertEquals(setOf("criatura_004", "criatura_008"), resultado.recompensasIds.toSet())
        assertEquals(1, progreso.guardados)
        val guardado = progreso.progreso
        assertTrue(registrar(jardin, museo).getOrThrow().recompensasIds.isEmpty())
        assertEquals(guardado, progreso.progreso)
    }

    @Test
    fun dosLecturasSimultaneasConcedenCadaRecompensaUnaSolaVez() = runBlocking {
        val progreso = ProgresoEnMemoria()
        val registrar = RegistrarObraUseCase(progreso, HomeRepositoryImpl(), catalogo)
        val museo = museosRepository.obtenerMuseos().first()
        coroutineScope {
            catalogo.obtenerObras().take(2).map { obra ->
                async { registrar(obra, museo).getOrThrow() }
            }.awaitAll()
        }
        assertEquals(setOf("criatura_001", "criatura_004", "criatura_008"),
            progreso.progreso.criaturasDescubiertas.toSet())
        assertEquals(3, progreso.progreso.criaturasDescubiertas.size)
        assertEquals(2, progreso.progreso.misionesCompletadas.size)
    }

    @Test
    fun elCatalogoTieneUnaODosMisionesYRecompensasDelMuseo() = runBlocking {
        val criaturas = HomeRepositoryImpl().traerCriaturas().map { it.id }
        val museos = museosRepository.obtenerMuseos()
        assertTrue(museos.all { it.misiones.size in 1..2 })
        val ids = museos.flatMap { it.misiones }.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
        museos.forEach { museo ->
            val recompensas = museo.misiones.mapNotNull { it.recompensaCriaturaId } +
                listOfNotNull(museo.recompensaMisionesId)
            assertTrue(recompensas.all { it in museo.criaturasIds && it in criaturas })
        }
        assertEquals(listOf("criatura_015"), museos[2].criaturasIds)
        assertEquals("criatura_015", museos[2].recompensaMisionesId)
    }

    @Test
    fun elCatalogoPermiteCompletarLasMisionesDeTodosLosMuseos() = runBlocking {
        val obras = catalogo.obtenerObras()
        val museos = museosRepository.obtenerMuseos()
        assertEquals(obras.size, obras.map { it.id }.distinct().size)
        assertTrue(obras.all { obra -> museos.any { it.id == obra.museoId } })
        museos.forEach { museo ->
            val progreso = ProgresoEnMemoria(ProgresoUsuario("criatura_012", listOf("criatura_012")))
            val registrar = RegistrarObraUseCase(progreso, HomeRepositoryImpl(), catalogo)
            val propias = obras.filter { it.museoId == museo.id }
            assertEquals(2, propias.size, museo.nombre)
            assertTrue(evaluar(museo, obras, progreso.progreso).misiones.all { it.disponible }, museo.nombre)
            propias.forEach { registrar(it, museo).getOrThrow() }
            assertEquals(museo.misiones.map { it.id }.toSet(), progreso.progreso.misionesCompletadas.toSet())
            assertTrue(museo.criaturasIds.all { it in progreso.progreso.criaturasDescubiertas }, museo.nombre)
        }
    }

    @Test
    fun soloAdmiteCantidadesPositivasYGruposDeUnaACuatroObras() {
        assertFailsWith<IllegalArgumentException> { ObjetivoMision.CantidadObras(0) }
        assertFailsWith<IllegalArgumentException> { ObjetivoMision.ObrasConcretas(emptyList()) }
        assertFailsWith<IllegalArgumentException> { ObjetivoMision.ObrasConcretas(listOf("a", "a")) }
        assertFailsWith<IllegalArgumentException> {
            ObjetivoMision.ObrasConcretas(listOf("a", "b", "c", "d", "e"))
        }
        val anterior = Json.decodeFromString<ProgresoUsuario>("""{"obrasEscaneadas":["obra_002"]}""")
        assertTrue(anterior.misionesCompletadas.isEmpty())
    }
}
