
package com.example.ars.features.museos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MuseosViewModel(
    private val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MuseosUiState())

    val state: StateFlow<MuseosUiState> = _state.asStateFlow()

    fun onAction(action: MuseosAction) {
        when (action) {
            MuseosAction.CargarMuseos -> cargarMuseos()
        }
    }

    private fun cargarMuseos() {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    cargando = true,
                    error = null
                )
            }

            val resultado = obtenerCatalogoMuseosUseCase()

            resultado.fold(
                onSuccess = { museos ->
                    _state.update {
                        it.copy(
                            museos = museos,
                            cargando = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            cargando = false,
                            error = error.message
                                ?: "No se pudieron cargar los museos"
                        )
                    }
                }
            )
        }
    }
}
