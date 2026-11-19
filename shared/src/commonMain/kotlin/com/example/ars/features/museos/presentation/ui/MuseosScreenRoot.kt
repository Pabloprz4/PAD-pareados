package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ars.shared.generated.resources.*
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.presentation.MuseosAction
import com.example.ars.features.museos.presentation.MuseosViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun MuseosScreenRoot(
    viewModel: MuseosViewModel,
    onVerObras: () -> Unit,
    onMuseoClick: (Museo) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(MuseosAction.CargarMuseos)
    }

    when {
        state.cargando -> {
            CircularProgressIndicator()
        }

        state.error != null -> {
            Column {
                Text(
                    text = stringResource(
                        Res.string.museo_error
                    )
                )

                TextButton(
                    onClick = {
                        viewModel.onAction(
                            MuseosAction.CargarMuseos
                        )
                    }
                ) {
                    Text(
                        text = stringResource(
                            Res.string.museo_reintentar
                        )
                    )
                }
            }
        }

        else -> {
            MuseosScreen(
                museos = state.museos,
                museosVisitados = state.progreso.museosVisitados,
                onVerObras = onVerObras,
                onMuseoClick = onMuseoClick
            )
        }
    }
}