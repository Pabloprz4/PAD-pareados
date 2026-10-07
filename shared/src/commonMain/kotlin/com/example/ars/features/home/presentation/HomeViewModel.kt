package com.example.ars.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.domain.usecase.ObtenerMuseosUseCase

data class HomeUiState(
    val cargando: Boolean = false,
    val museos: List<Museo> = emptyList(),
    val criaturas: List<Criatura> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val obtenerCriaturasUseCase: ObtenerCriaturasUseCase,
    private val obtenerMuseosUseCase: ObtenerMuseosUseCase) {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main)

    fun onCargarCriaturasClick() {
        scope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            obtenerCriaturasUseCase()
                .onSuccess { lista ->
                    uiState = uiState.copy(cargando = false, criaturas = lista)
                }
                .onFailure { excepcion ->
                    uiState = uiState.copy(cargando = false, error = excepcion.message)
                }
        }
    }

    fun cargarMuseos() {
        scope.launch {
            uiState = uiState.copy(cargando = true, error = null)

            obtenerMuseosUseCase()
                .onSuccess { lista ->
                    uiState = uiState.copy(
                        cargando = false,
                        museos = lista
                    )
                }
                .onFailure { excepcion ->
                    uiState = uiState.copy(
                        cargando = false,
                        error = excepcion.message
                    )
                }
        }
    }
}