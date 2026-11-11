
package com.example.ars.features.museos.presentation.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.museo_error
import ars.shared.generated.resources.museo_reintentar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.presentation.MuseosAction
import com.example.ars.features.museos.presentation.MuseosViewModel

@Composable
fun MuseosScreenRoot(
    viewModel: MuseosViewModel,
    onMuseoClick: (Museo) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(MuseosAction.CargarMuseos)
    }

    when {
        state.cargando -> CircularProgressIndicator()
        state.error != null -> Column {
            Text(stringResource(Res.string.museo_error))
            TextButton(onClick = { viewModel.onAction(MuseosAction.CargarMuseos) }) {
                Text(stringResource(Res.string.museo_reintentar))
            }
        }
        else -> MuseosScreen(
            museos = state.museos,
            museosVisitados = state.progreso.museosVisitados,
            onMuseoClick = onMuseoClick
        )
    }
}
