package com.example.ars.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerMuseosUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase,
    private val obtenerMuseosUseCase: ObtenerMuseosUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var contenidoSolicitado = false

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.CargarContenido -> cargarContenido()
        }
    }

    private fun cargarContenido() {
        // La composición puede volver a entrar con el mismo ViewModel.
        if (contenidoSolicitado) return
        contenidoSolicitado = true
        _state.update { it.copy(cargando = true, error = null) }

        viewModelScope.launch {
            try {
                coroutineScope {
                    val cargaMuseos = async { obtenerMuseosUseCase() }
                    val cargaCriaturas = async { obtenerCriaturasUseCase() }
                    val museos = cargaMuseos.await()
                    val criaturas = cargaCriaturas.await()
                    val errores = listOfNotNull(
                        museos.exceptionOrNull()?.let {
                            it.message ?: "No se han podido cargar los museos"
                        },
                        criaturas.exceptionOrNull()?.let {
                            it.message ?: "No se han podido cargar las criaturas"
                        }
                    )

                    _state.update {
                        it.copy(
                            museos = museos.getOrNull() ?: it.museos,
                            criaturas = criaturas.getOrNull() ?: it.criaturas,
                            error = errores.takeIf { lista -> lista.isNotEmpty() }
                                ?.joinToString("\n")
                        )
                    }
                }
            } finally {
                // Ambas cargas han terminado o el ciclo de vida las ha cancelado.
                _state.update { it.copy(cargando = false) }
            }
        }
    }
}
