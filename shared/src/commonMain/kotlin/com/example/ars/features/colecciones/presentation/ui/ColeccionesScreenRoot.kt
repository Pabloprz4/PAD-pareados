package com.example.ars.features.colecciones.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.colecciones.presentation.ColeccionesAction
import com.example.ars.features.colecciones.presentation.ColeccionesViewModel

@Composable
fun ColeccionesScreenRoot(
    viewModel: ColeccionesViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(ColeccionesAction.CargarContenido)
    }

    ColeccionesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}