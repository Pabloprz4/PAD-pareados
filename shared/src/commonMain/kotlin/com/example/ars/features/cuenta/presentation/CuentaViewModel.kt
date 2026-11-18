package com.example.ars.features.cuenta.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ars.shared.generated.resources.*
import com.example.ars.features.cuenta.domain.CuentaRepository
import com.example.ars.features.cuenta.domain.ErrorCuenta
import com.example.ars.features.cuenta.domain.FalloCuenta
import com.example.ars.features.cuenta.domain.SesionCuenta
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CuentaViewModel(private val repository: CuentaRepository) : ViewModel() {
    private val _state = MutableStateFlow(CuentaUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sesion.collect { sesion ->
                _state.update {
                    when (sesion) {
                        is SesionCuenta.Activa ->
                            it.copy(sesion = sesion, clave = "", mensaje = null, esError = false)
                        SesionCuenta.FalloRenovacion -> it.copy(
                            // Si ya había una cuenta abierta, esperamos sin volver al inicio de sesión.
                            sesion = (it.sesion as? SesionCuenta.Activa) ?: SesionCuenta.SinCuenta,
                            mensaje = Res.string.cuenta_error_renovar,
                            esError = true
                        )
                        SesionCuenta.SinCuenta -> it.copy(
                            sesion = sesion,
                            mensaje = it.mensaje.takeUnless { mensaje -> mensaje == Res.string.cuenta_error_renovar }
                        )
                        SesionCuenta.Cargando -> it.copy(sesion = sesion)
                    }
                }
            }
        }
    }

    fun onAction(action: CuentaAction) {
        if (_state.value.cargando) return
        when (action) {
            is CuentaAction.CambiarCorreo -> _state.update { it.copy(correo = action.texto, mensaje = null) }
            is CuentaAction.CambiarClave -> _state.update { it.copy(clave = action.texto, mensaje = null) }
            CuentaAction.CambiarModo -> _state.update { it.copy(registro = !it.registro, mensaje = null) }
            CuentaAction.LimpiarFormulario -> _state.update {
                CuentaUiState(sesion = it.sesion)
            }
            CuentaAction.Enviar -> enviar()
            CuentaAction.Salir -> salir()
        }
    }

    private fun enviar() {
        val estado = _state.value
        val correo = estado.correo.trim()
        val error = when {
            !Regex("[^ @]+@[^ @]+\\.[^ @]+").matches(correo) -> Res.string.cuenta_correo_invalido
            estado.registro && estado.clave.length < 6 -> Res.string.cuenta_clave_corta
            estado.clave.isEmpty() -> Res.string.cuenta_clave_vacia
            else -> null
        }
        if (error != null) {
            _state.update { it.copy(mensaje = error, esError = true) }
            return
        }

        _state.update { it.copy(cargando = true, mensaje = null) }
        viewModelScope.launch {
            try {
                if (estado.registro) {
                    val confirmar = repository.registrar(correo, estado.clave)
                    if (confirmar) {
                        _state.update {
                            it.copy(registro = false, clave = "", mensaje = Res.string.cuenta_confirmar, esError = false)
                        }
                    }
                } else {
                    repository.entrar(correo, estado.clave)
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (fallo: Exception) {
                val mensaje = when ((fallo as? FalloCuenta)?.motivo) {
                    ErrorCuenta.Credenciales -> Res.string.cuenta_credenciales
                    ErrorCuenta.CorreoSinConfirmar -> Res.string.cuenta_sin_confirmar
                    ErrorCuenta.CorreoYaRegistrado -> Res.string.cuenta_ya_registrado
                    ErrorCuenta.CorreoNoAutorizado -> Res.string.cuenta_correo_no_autorizado
                    ErrorCuenta.DemasiadosIntentos -> Res.string.cuenta_demasiados_intentos
                    else -> Res.string.cuenta_error
                }
                _state.update { it.copy(mensaje = mensaje, esError = true) }
            } finally {
                _state.update { it.copy(cargando = false) }
            }
        }
    }

    private fun salir() {
        _state.update { it.copy(cargando = true, mensaje = null) }
        viewModelScope.launch {
            try {
                repository.salir()
                _state.update { CuentaUiState(sesion = it.sesion, cargando = true) }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _state.update { it.copy(mensaje = Res.string.cuenta_error_salir, esError = true) }
            } finally {
                _state.update { it.copy(cargando = false) }
            }
        }
    }
}
