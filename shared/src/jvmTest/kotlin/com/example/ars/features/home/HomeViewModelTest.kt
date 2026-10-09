package com.example.ars.features.home

import androidx.lifecycle.ViewModelStore
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerMuseosUseCase
import com.example.ars.features.home.presentation.HomeAction
import com.example.ars.features.home.presentation.HomeViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HomeViewModelTest {
    private val museos = listOf(Museo("Prado", "Madrid", 0, 10, 0, 3))
    private val criaturas = listOf(Criatura("Menina", "Prado", "Descripción"))

    @Test
    fun cargaEsperaAmbasListasCuandoMuseosTerminaPrimero() = comprobarOrden(museosPrimero = true)

    @Test
    fun cargaEsperaAmbasListasCuandoCriaturasTerminaPrimero() = comprobarOrden(museosPrimero = false)

    private fun comprobarOrden(museosPrimero: Boolean) = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val repository = RepositorioControlado()
                val viewModel = crearViewModel(repository)
                val store = ViewModelStore().apply { put("home", viewModel) }
                try {
                    viewModel.onAction(HomeAction.CargarContenido)
                    repository.museosIniciados.await()
                    repository.criaturasIniciadas.await()
                    // Reentrar en la composición no debe duplicar las peticiones.
                    viewModel.onAction(HomeAction.CargarContenido)
                    assertEquals(1, repository.peticionesMuseos)
                    assertEquals(1, repository.peticionesCriaturas)
                    if (museosPrimero) repository.museos.complete(museos)
                    else repository.criaturas.complete(criaturas)
                    yield()
                    assertTrue(viewModel.state.value.cargando)
                    if (museosPrimero) repository.criaturas.complete(criaturas)
                    else repository.museos.complete(museos)
                    val state = viewModel.state.first { !it.cargando }
                    assertEquals(museos, state.museos)
                    assertEquals(criaturas, state.criaturas)
                    assertNull(state.error)
                } finally {
                    store.clear()
                }
            }
        }
    }

    @Test
    fun errorDeRepositorioMantieneLosDatosDeLaOtraCarga() = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val repository = RepositorioControlado()
                val viewModel = crearViewModel(repository)
                val store = ViewModelStore().apply { put("home", viewModel) }
                try {
                    viewModel.onAction(HomeAction.CargarContenido)
                    repository.museos.completeExceptionally(IllegalStateException("Fallo de museos"))
                    repository.criaturas.complete(criaturas)
                    val state = viewModel.state.first { !it.cargando }
                    assertEquals("Fallo de museos", state.error)
                    assertEquals(criaturas, state.criaturas)
                    assertTrue(state.museos.isEmpty())
                } finally {
                    store.clear()
                }
            }
        }
    }

    @Test
    fun ambosErroresSeMuestranInclusoSinMensajeDeExcepcion() = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val repository = RepositorioControlado()
                val viewModel = crearViewModel(repository)
                val store = ViewModelStore().apply { put("home", viewModel) }
                try {
                    viewModel.onAction(HomeAction.CargarContenido)
                    repository.museos.completeExceptionally(IllegalStateException())
                    repository.criaturas.completeExceptionally(IllegalArgumentException("Fallo de criaturas"))
                    val state = viewModel.state.first { !it.cargando }
                    assertEquals("No se han podido cargar los museos\nFallo de criaturas", state.error)
                } finally {
                    store.clear()
                }
            }
        }
    }

    @Test
    fun destruirViewModelCancelaAmbasCargasSinPublicarUnError() = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val repository = RepositorioControlado()
                val viewModel = crearViewModel(repository)
                val store = ViewModelStore().apply { put("home", viewModel) }
                try {
                    viewModel.onAction(HomeAction.CargarContenido)
                    repository.museosIniciados.await()
                    repository.criaturasIniciadas.await()
                    store.clear()
                    repository.museosFinalizados.await()
                    repository.criaturasFinalizadas.await()
                    val state = viewModel.state.first { !it.cargando }
                    assertNull(state.error)
                    assertFalse(repository.museos.isCompleted)
                    assertFalse(repository.criaturas.isCompleted)
                } finally {
                    store.clear()
                }
            }
        }
    }

    @Test
    fun casosDeUsoPropaganLaCancelacion(): Unit = runBlocking {
        val repository = object : HomeRepository {
            override suspend fun traerMuseos(): List<Museo> = throw CancellationException("cancelado")
            override suspend fun traerCriaturas(): List<Criatura> = throw CancellationException("cancelado")
        }
        assertFailsWith<CancellationException> { ObtenerMuseosUseCase(repository)() }
        assertFailsWith<CancellationException> { ObtenerCriaturasUseCase(repository)() }
    }

    private fun crearViewModel(repository: HomeRepository) = HomeViewModel(
        obtenerCriaturasUseCase = ObtenerCriaturasUseCase(repository),
        obtenerMuseosUseCase = ObtenerMuseosUseCase(repository)
    )

    private class RepositorioControlado : HomeRepository {
        val museos = CompletableDeferred<List<Museo>>()
        val criaturas = CompletableDeferred<List<Criatura>>()
        val museosIniciados = CompletableDeferred<Unit>()
        val criaturasIniciadas = CompletableDeferred<Unit>()
        val museosFinalizados = CompletableDeferred<Unit>()
        val criaturasFinalizadas = CompletableDeferred<Unit>()
        var peticionesMuseos = 0
        var peticionesCriaturas = 0

        override suspend fun traerMuseos(): List<Museo> {
            peticionesMuseos++
            museosIniciados.complete(Unit)
            return try { museos.await() } finally { museosFinalizados.complete(Unit) }
        }

        override suspend fun traerCriaturas(): List<Criatura> {
            peticionesCriaturas++
            criaturasIniciadas.complete(Unit)
            return try { criaturas.await() } finally { criaturasFinalizadas.complete(Unit) }
        }
    }
}
