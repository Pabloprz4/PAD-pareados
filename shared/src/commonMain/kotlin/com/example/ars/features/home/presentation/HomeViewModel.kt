package com.example.ars.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase,
    private val obtenerMuseosUseCase: ObtenerCatalogoMuseosUseCase,
    private val obtenerProgresoUseCase: ObtenerProgresoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.CargarContenido -> cargarContenido()
        }
    }

    private fun cargarContenido() {
        // al volver del escáner volvemos a leer las visitas y criaturas guardadas
        if (_state.value.cargando) return

        _state.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                coroutineScope {
                    val cargaMuseos = async {
                        obtenerMuseosUseCase()
                    }

                    val cargaCriaturas = async {
                        obtenerCriaturasUseCase()
                    }

                    val cargaProgreso = async {
                        obtenerProgresoUseCase()
                    }

                    val museos = cargaMuseos.await()
                    val criaturas = cargaCriaturas.await()
                    val progreso = cargaProgreso.await()

                    val errores = listOfNotNull(
                        museos.exceptionOrNull()?.let {
                            it.message
                                ?: "No se han podido cargar los museos."
                        },
                        criaturas.exceptionOrNull()?.let {
                            it.message
                                ?: "No se han podido cargar las criaturas."
                        },
                        progreso.exceptionOrNull()?.let {
                            it.message
                                ?: "No se ha podido cargar tu progreso."
                        }
                    )

                    _state.update {
                        it.copy(
                            museos = museos.getOrNull() ?: it.museos,
                            criaturas = criaturas.getOrNull() ?: it.criaturas,
                            progreso = progreso.getOrNull() ?: it.progreso,
                            error = errores
                                .takeIf { lista -> lista.isNotEmpty() }
                                ?.joinToString("\n")
                        )
                    }
                }
            } finally {
                _state.update {
                    it.copy(cargando = false)
                }
            }
        }
    }
}