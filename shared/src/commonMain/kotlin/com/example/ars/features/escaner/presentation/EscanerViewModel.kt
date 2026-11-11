package com.example.ars.features.escaner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase
import com.example.ars.features.museos.domain.usecase.RegistrarVisitaMuseoUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EscanerViewModel(
    private val validarQrUseCase: ValidarQrUseCase,
    private val registrarVisitaMuseoUseCase: RegistrarVisitaMuseoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EscanerUiState())
    val state = _state.asStateFlow()

    fun onAction(action: EscanerAction) {
        when (action) {
            EscanerAction.LeerQr -> {
                if (!_state.value.leyendo && !_state.value.validando) {
                    _state.update { it.copy(leyendo = true, error = false) }
                }
            }
            is EscanerAction.QrLeido -> validar(action.contenido)
            EscanerAction.LecturaCancelada -> {
                // cancelar conserva el museo y el último resultado
                _state.update { it.copy(leyendo = false) }
            }
            EscanerAction.MuseoAbierto -> {
                _state.update { it.copy(visitaPorAbrir = null) }
            }
            EscanerAction.LecturaFallida -> {
                _state.update {
                    it.copy(leyendo = false, contenido = null, resultado = null, error = true)
                }
            }
        }
    }

    private fun validar(contenido: String?) {
        if (!_state.value.leyendo || _state.value.validando) return
        _state.update {
            it.copy(leyendo = false, validando = true, contenido = contenido, resultado = null)
        }
        viewModelScope.launch {
            try {
                val resultado = validarQrUseCase(
                    contenido = contenido,
                    museoActivoId = _state.value.museoActivo?.id
                )
                // solo abrimos la ficha cuando la visita se ha guardado
                val visita = if (resultado is ResultadoValidacionQr.MuseoValido) {
                    registrarVisitaMuseoUseCase(resultado.museo).getOrThrow()
                } else {
                    null
                }
                _state.update {
                    it.copy(
                        resultado = resultado,
                        visitaPorAbrir = visita,
                        // solo un QR de museo válido cambia el museo activo
                        museoActivo = if (resultado is ResultadoValidacionQr.MuseoValido) {
                            resultado.museo
                        } else it.museoActivo
                    )
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _state.update { it.copy(error = true) }
            } finally {
                _state.update { it.copy(validando = false) }
            }
        }
    }
}
