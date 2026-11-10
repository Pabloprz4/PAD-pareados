package com.example.ars.features.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.home.presentation.PrimeraCriaturaAction
import com.example.ars.features.home.presentation.PrimeraCriaturaViewModel

@Composable
fun PrimeraCriaturaScreenRoot(
    viewModel: PrimeraCriaturaViewModel,
    onEleccionCompletada: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(
            PrimeraCriaturaAction.CargarContenido
        )
    }

    LaunchedEffect(state.eleccionCompletada) {
        if (state.eleccionCompletada) {
            onEleccionCompletada()
        }
    }

    PrimeraCriaturaScreen(
        state = state,
        onAction = viewModel::onAction
    )
}