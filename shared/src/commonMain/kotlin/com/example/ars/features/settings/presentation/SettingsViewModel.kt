package com.example.ars.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.core.storage.guardarTextoLocal
import com.example.ars.core.storage.leerTextoLocal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val escrituraTema = Mutex()

    private companion object {
        const val CLAVE_TEMA_OSCURO = "tema_oscuro"
        const val CLAVE_IDIOMA = "idioma_preferido"
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
    }

    init {
        cargarAjustes()
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.CambiarTemaOscuro -> {
                _state.update { it.copy(modoOscuro = action.activado) }
                viewModelScope.launch {
                    escrituraTema.withLock {
                        guardarTextoLocal(CLAVE_TEMA_OSCURO, _state.value.modoOscuro.toString())
                    }
                }
            }
            is SettingsAction.CambiarNotificaciones -> {
                _state.update { it.copy(notificacionesMisiones = action.activadas) }
            }
            is SettingsAction.CambiarIdioma -> {
                guardarIdioma(action.nuevoIdioma)
            }
            is SettingsAction.CambiarNombreUsuario -> {
                guardarNombreUsuario(action.nuevoNombre)
            }
        }
    }

    private fun cargarAjustes() {
        viewModelScope.launch {
            _state.update { it.copy(cargando = true) }
            val temaGuardado = leerTextoLocal(CLAVE_TEMA_OSCURO) == "true"
            val idiomaGuardado = leerTextoLocal(CLAVE_IDIOMA) ?: "es"
            val nombreGuardado = leerTextoLocal(CLAVE_NOMBRE_USUARIO) ?: ""
            _state.update {
                it.copy(
                    modoOscuro = temaGuardado,
                    idioma = idiomaGuardado,
                    nombreUsuario = nombreGuardado,
                    cargando = false
                )
            }
        }
    }

    private fun guardarIdioma(nuevoIdioma: String) {
        _state.update { it.copy(idioma = nuevoIdioma) }
        viewModelScope.launch {
            guardarTextoLocal(CLAVE_IDIOMA, nuevoIdioma)
        }
    }

    private fun guardarNombreUsuario(nuevoNombre: String) {
        _state.update { it.copy(nombreUsuario = nuevoNombre) }
        viewModelScope.launch {
            guardarTextoLocal(CLAVE_NOMBRE_USUARIO, nuevoNombre)
        }
    }
}
