
package com.example.ars.features.museos.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.presentation.HomeAction
import com.example.ars.features.home.presentation.HomeViewModel

@Composable
fun MuseosScreenRoot(
    viewModel: HomeViewModel,
    onMuseoClick: (Museo) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(HomeAction.CargarContenido)
    }

    MuseosScreen(
        museos = state.museos,
        onMuseoClick = onMuseoClick
    )
}
