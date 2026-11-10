package com.example.ars.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.home.domain.usecase.ElegirCriaturaInicialUseCase
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrimeraCriaturaViewModel(
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase,
    private val obtenerProgresoUseCase: ObtenerProgresoUseCase,
    private val elegirCriaturaInicialUseCase: ElegirCriaturaInicialUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PrimeraCriaturaUiState())
    val state = _state.asStateFlow()

    private var operacion: Job? = null
    private var contenidoCargado = false
    private var falloAlGuardar = false

    fun onAction(action: PrimeraCriaturaAction) {
        when (action) {
            PrimeraCriaturaAction.CargarContenido -> {
                if (!contenidoCargado) {
                    cargarContenido()
                }
            }

            is PrimeraCriaturaAction.SeleccionarCriatura -> {
                seleccionarCriatura(action.criaturaId)
            }

            PrimeraCriaturaAction.ConfirmarEleccion -> {
                confirmarEleccion()
            }

            PrimeraCriaturaAction.Reintentar -> {
                if (_state.value.error != null) {
                    if (falloAlGuardar) {
                        confirmarEleccion()
                    } else {
                        cargarContenido()
                    }
                }
            }
        }
    }

    private fun cargarContenido() {
        if (operacion?.isActive == true) return

        _state.update {
            it.copy(cargando = true, error = null)
        }

        operacion = viewModelScope.launch {
            try {
                val progreso = obtenerProgresoUseCase().getOrThrow()

                // Si ya eligió, no necesitamos mostrar las opciones.
                if (progreso.criaturaInicialId != null) {
                    contenidoCargado = true

                    _state.update {
                        it.copy(
                            eleccionCompletada = true,
                            criaturaSeleccionadaId = progreso.criaturaInicialId
                        )
                    }

                    return@launch
                }

                val catalogo = obtenerCriaturasUseCase().getOrThrow()

                val opciones = ElegirCriaturaInicialUseCase.IDS_INICIALES.map { id ->
                    catalogo.firstOrNull { it.id == id }
                        ?: error("No se ha encontrado una criatura inicial.")
                }

                contenidoCargado = true
                falloAlGuardar = false

                _state.update {
                    it.copy(
                        opciones = opciones,
                        error = null
                    )
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                falloAlGuardar = false

                _state.update {
                    it.copy(
                        error = "No se han podido cargar los datos. Inténtalo de nuevo."
                    )
                }
            } finally {
                _state.update {
                    it.copy(cargando = false)
                }
            }
        }
    }

    private fun seleccionarCriatura(criaturaId: String) {
        val estado = _state.value

        if (estado.cargando || estado.guardando || estado.eleccionCompletada) {
            return
        }

        if (estado.opciones.none { it.id == criaturaId }) return

        falloAlGuardar = false

        _state.update {
            it.copy(
                criaturaSeleccionadaId = criaturaId,
                error = null
            )
        }
    }

    private fun confirmarEleccion() {
        val estado = _state.value

        if (operacion?.isActive == true) return
        if (estado.cargando || estado.guardando || estado.eleccionCompletada) return

        val criaturaId = estado.criaturaSeleccionadaId ?: return

        if (estado.opciones.none { it.id == criaturaId }) return

        _state.update {
            it.copy(
                guardando = true,
                error = null
            )
        }

        operacion = viewModelScope.launch {
            try {
                elegirCriaturaInicialUseCase(criaturaId).getOrThrow()

                falloAlGuardar = false

                _state.update {
                    it.copy(eleccionCompletada = true)
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                falloAlGuardar = true

                _state.update {
                    it.copy(
                        error = "No se ha podido guardar tu elección. Inténtalo de nuevo."
                    )
                }
            } finally {
                _state.update {
                    it.copy(guardando = false)
                }
            }
        }
    }
}