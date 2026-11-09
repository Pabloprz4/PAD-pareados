package com.example.ars.features.escaner

import androidx.lifecycle.ViewModelStore
import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase
import com.example.ars.features.escaner.presentation.EscanerAction
import com.example.ars.features.escaner.presentation.EscanerViewModel
import com.example.ars.features.museos.domain.model.Museo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EscanerViewModelTest {
    @Test
    fun activaElMuseoYPermiteRepetirLecturas() = comprobar { vm ->
        leer(vm, "ars:v1:museo:museo_001")
        assertEquals("museo_001", vm.state.value.museoActivo?.id)
        leer(vm, "ars:v1:obra:obra_001")
        val resultado = assertIs<ResultadoValidacionQr.ObraValida>(vm.state.value.resultado)
        assertEquals("Las meninas", resultado.obra.titulo)
        leer(vm, "ars:v1:obra:obra_001")
        assertEquals(resultado, vm.state.value.resultado)
        leer(vm, "ars:v1:obra:obra_003")
        assertIs<ResultadoValidacionQr.MuseoIncorrecto>(vm.state.value.resultado)
        assertEquals("museo_001", vm.state.value.museoActivo?.id)
        leer(vm, "ars:v1:museo:museo_002")
        leer(vm, "ars:v1:obra:obra_003")
        assertEquals("Guernica", assertIs<ResultadoValidacionQr.ObraValida>(vm.state.value.resultado).obra.titulo)
    }

    @Test
    fun rechazosYCancelacionConservanElMuseo() = comprobar { vm ->
        leer(vm, "ars:v1:obra:obra_001")
        assertEquals(ResultadoValidacionQr.RequiereEscanearMuseo, vm.state.value.resultado)
        assertNull(vm.state.value.museoActivo)
        leer(vm, "ars:v1:museo:museo_001")
        val museo = vm.state.value.museoActivo
        leer(vm, "ars:v1:museo:desconocido")
        assertEquals(ResultadoValidacionQr.MuseoDesconocido, vm.state.value.resultado)
        leer(vm, null)
        assertEquals(ResultadoValidacionQr.CodigoInvalido, vm.state.value.resultado)
        vm.onAction(EscanerAction.LeerQr)
        vm.onAction(EscanerAction.LecturaCancelada)
        assertFalse(vm.state.value.leyendo)
        assertEquals(museo, vm.state.value.museoActivo)
        assertEquals(ResultadoValidacionQr.CodigoInvalido, vm.state.value.resultado)
        assertFalse(vm.state.value.error)
    }

    @Test
    fun falloDelLectorPermiteReintentarSinPerderElMuseo() = comprobar { vm ->
        leer(vm, "ars:v1:museo:museo_001")
        vm.onAction(EscanerAction.LeerQr)
        vm.onAction(EscanerAction.LecturaFallida)
        assertFalse(vm.state.value.leyendo)
        assertTrue(vm.state.value.error)
        assertEquals("museo_001", vm.state.value.museoActivo?.id)
        assertNull(vm.state.value.resultado)
        leer(vm, "ars:v1:obra:obra_001")
        assertIs<ResultadoValidacionQr.ObraValida>(vm.state.value.resultado)
        assertFalse(vm.state.value.error)
    }

    @Test
    fun noValidaDosResultadosMientrasHayUnaConsultaPendiente() = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val consulta = CompletableDeferred<Museo?>()
                var peticiones = 0
                val repository = object : CatalogoRepository {
                    override suspend fun obtenerMuseoPorId(id: String): Museo? {
                        peticiones++
                        return consulta.await()
                    }
                    override suspend fun obtenerObraPorId(id: String): Obra? = null
                }
                val vm = EscanerViewModel(ValidarQrUseCase(InterpretarQrUseCase(), repository))
                val store = ViewModelStore().apply { put("escaner", vm) }
                try {
                    vm.onAction(EscanerAction.LeerQr)
                    vm.onAction(EscanerAction.QrLeido("ars:v1:museo:museo_001"))
                    vm.onAction(EscanerAction.LeerQr)
                    vm.onAction(EscanerAction.QrLeido("ars:v1:museo:museo_002"))
                    assertTrue(vm.state.value.validando)
                    assertFalse(vm.state.value.leyendo)
                    assertEquals(1, peticiones)
                    consulta.completeExceptionally(IllegalStateException("Fallo de catálogo"))
                    vm.state.first { !it.validando }
                    assertTrue(vm.state.value.error)
                    vm.onAction(EscanerAction.LeerQr)
                    assertFalse(vm.state.value.error)
                    assertTrue(vm.state.value.leyendo)
                } finally {
                    store.clear()
                }
            }
        }
    }

    private fun comprobar(block: suspend (EscanerViewModel) -> Unit) = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val vm = EscanerViewModel(DefaultAppContainer().validarQrUseCase)
                val store = ViewModelStore().apply { put("escaner", vm) }
                try {
                    block(vm)
                } finally {
                    store.clear()
                }
            }
        }
    }

    private suspend fun leer(vm: EscanerViewModel, contenido: String?) {
        vm.onAction(EscanerAction.LeerQr)
        vm.onAction(EscanerAction.QrLeido(contenido))
        vm.state.first { !it.validando }
    }
}
