package com.example.ars.features.cuenta

import androidx.lifecycle.ViewModelStore
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.cuenta_error_renovar
import com.example.ars.features.cuenta.domain.CuentaRepository
import com.example.ars.features.cuenta.domain.SesionCuenta
import com.example.ars.features.cuenta.presentation.CuentaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CuentaViewModelTest {
    @Test
    fun renovarSesionConservaLaCuentaHastaQueSeCierra() = runBlocking {
        withTimeout(5_000) {
            withContext(Dispatchers.Main) {
                val repository = object : CuentaRepository {
                    override val sesion = MutableStateFlow<SesionCuenta>(SesionCuenta.Cargando)
                    override suspend fun entrar(correo: String, clave: String) = Unit
                    override suspend fun registrar(correo: String, clave: String) = false
                    override suspend fun salir() {
                        sesion.value = SesionCuenta.SinCuenta
                    }
                }
                val vm = CuentaViewModel(repository)
                val store = ViewModelStore().apply { put("cuenta", vm) }
                try {
                    val cuenta = SesionCuenta.Activa("usuario", "cuenta@example.com")
                    repository.sesion.value = SesionCuenta.FalloRenovacion
                    val arranque = vm.state.first { it.mensaje == Res.string.cuenta_error_renovar }
                    assertEquals(SesionCuenta.SinCuenta, arranque.sesion)

                    repository.sesion.value = cuenta
                    vm.state.first { it.sesion == cuenta }

                    repository.sesion.value = SesionCuenta.FalloRenovacion
                    val fallo = vm.state.first { it.mensaje == Res.string.cuenta_error_renovar }
                    assertEquals(cuenta, fallo.sesion)

                    repository.sesion.value = cuenta
                    val recuperada = vm.state.first { it.sesion == cuenta && it.mensaje == null }
                    assertNull(recuperada.mensaje)

                    repository.salir()
                    assertEquals(SesionCuenta.SinCuenta, vm.state.first { it.sesion == SesionCuenta.SinCuenta }.sesion)
                } finally {
                    store.clear()
                }
            }
        }
    }
}
