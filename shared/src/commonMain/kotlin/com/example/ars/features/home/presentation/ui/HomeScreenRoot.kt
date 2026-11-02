package com.example.ars.features.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.home.presentation.HomeAction
import com.example.ars.features.home.presentation.HomeViewModel

@Composable
fun HomeScreenRoot(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // pedimos los datos al entrar con este viewmodel, no en cada recomposición
    LaunchedEffect(viewModel) {
        viewModel.onAction(HomeAction.CargarContenido)
    }

    HomeScreen(state = state)
}
