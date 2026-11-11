package com.example.ars.features.museos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MuseosViewModel(
    private val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase,
    private val obtenerProgresoUseCase: ObtenerProgresoUseCase,
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MuseosUiState())
    val state = _state.asStateFlow()

    fun onAction(action: MuseosAction) {
        when (action) {
            MuseosAction.CargarMuseos -> cargarMuseos()
        }
    }

    private fun cargarMuseos() {
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            try {
                val museos = obtenerCatalogoMuseosUseCase().getOrThrow()
                val progreso = obtenerProgresoUseCase().getOrThrow()
                val criaturas = obtenerCriaturasUseCase().getOrThrow()
                _state.update {
                    it.copy(museos = museos, progreso = progreso, criaturas = criaturas)
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _state.update { it.copy(error = "No se han podido cargar los museos.") }
            } finally {
                _state.update { it.copy(cargando = false) }
            }
        }
    }
}
