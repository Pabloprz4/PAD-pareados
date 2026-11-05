
package com.example.ars.features.museos.presentation.ui

import androidx.compose.runtime.Composable
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

    MuseosScreen(
        museos = state.museos,
        onMuseoClick = onMuseoClick
    )
}
