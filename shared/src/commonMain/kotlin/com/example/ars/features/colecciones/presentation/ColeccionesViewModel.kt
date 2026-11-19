package com.example.ars.features.colecciones.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ColeccionesViewModel(
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase,
    private val obtenerProgresoUseCase: ObtenerProgresoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ColeccionesUiState())
    val state = _state.asStateFlow()

    private var carga: Job? = null

    fun onAction(action: ColeccionesAction) {
        when (action) {
            ColeccionesAction.CargarContenido -> cargarContenido()
            ColeccionesAction.Reintentar -> cargarContenido()
        }
    }

    private fun cargarContenido() {
        if (carga?.isActive == true) return

        _state.update {
            it.copy(
                cargando = true,
                error = false
            )
        }

        carga = viewModelScope.launch {
            try {
                coroutineScope {
                    val cargaCriaturas = async {
                        obtenerCriaturasUseCase()
                    }

                    val cargaProgreso = async {
                        obtenerProgresoUseCase()
                    }

                    val criaturas = cargaCriaturas.await().getOrThrow()
                    val progreso = cargaProgreso.await().getOrThrow()

                    _state.update {
                        it.copy(
                            criaturas = criaturas,
                            progreso = progreso
                        )
                    }
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        error = true
                    )
                }
            } finally {
                _state.update {
                    it.copy(cargando = false)
                }
            }
        }
    }
}