package com.example.ars.features.escaner.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.ars.features.museos.domain.model.ResultadoVisitaMuseo
import com.example.ars.features.escaner.domain.model.ResultadoRegistroObra
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.escaner.presentation.EscanerAction
import com.example.ars.features.escaner.presentation.EscanerViewModel

@Composable
fun EscanerScreenRoot(
    viewModel: EscanerViewModel,
    onLeerQr: (onLeido: (String?) -> Unit, onCancelado: () -> Unit, onError: () -> Unit) -> Unit,
    onMuseoEscaneado: (ResultadoVisitaMuseo) -> Unit,
    onObraEscaneada: (ResultadoRegistroObra) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.visitaPorAbrir) {
        state.visitaPorAbrir?.let { visita ->
            onMuseoEscaneado(visita)
            viewModel.onAction(EscanerAction.MuseoAbierto)
        }
    }

    LaunchedEffect(state.obraPorAbrir) {
        state.obraPorAbrir?.let { registro ->
            onObraEscaneada(registro)
            viewModel.onAction(EscanerAction.ObraAbierta)
        }
    }

    EscanerScreen(
        state = state,
        onAction = { action ->
            if (action == EscanerAction.LeerQr) {
                // miramos el estado actual para no abrir dos lectores con dos clics seguidos
                if (!viewModel.state.value.leyendo && !viewModel.state.value.validando) {
                    viewModel.onAction(action)
                    onLeerQr(
                        { viewModel.onAction(EscanerAction.QrLeido(it)) },
                        { viewModel.onAction(EscanerAction.LecturaCancelada) },
                        { viewModel.onAction(EscanerAction.LecturaFallida) }
                    )
                }
            } else {
                viewModel.onAction(action)
            }
        }
    )
}
