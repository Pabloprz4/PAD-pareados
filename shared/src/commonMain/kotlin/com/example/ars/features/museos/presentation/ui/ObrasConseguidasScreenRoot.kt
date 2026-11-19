package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ars.shared.generated.resources.*
import com.example.ars.features.museos.presentation.MuseosAction
import com.example.ars.features.museos.presentation.MuseosViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObrasConseguidasScreenRoot(
    viewModel: MuseosViewModel,
    onVolver: () -> Unit,
    onObraClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(MuseosAction.CargarMuseos)
    }

    when {
        state.cargando -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onVolver) {
                    Text(
                        stringResource(
                            Res.string.obras_conseguidas_volver
                        )
                    )
                }

                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onClick = onVolver) {
                    Text(
                        stringResource(
                            Res.string.obras_conseguidas_volver
                        )
                    )
                }

                Text(
                    stringResource(
                        Res.string.obras_conseguidas_error
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
                        stringResource(
                            Res.string.museo_reintentar
                        )
                    )
                }
            }
        }

        else -> {
            ObrasConseguidasScreen(
                obras = state.obrasConseguidas,
                onVolver = onVolver,
                onObraClick = onObraClick
            )
        }
    }
}