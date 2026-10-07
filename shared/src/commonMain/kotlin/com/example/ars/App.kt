package com.example.ars

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.ars.di.AppContainer
import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.home.presentation.HomeViewModel
import com.example.ars.features.home.presentation.ui.HomeScreen
import com.example.ars.core.ui.theme.ArsTheme

@Composable
fun App(
    appContainer: AppContainer = remember { DefaultAppContainer() }
) {
    ArsTheme {

        val homeViewModel = remember {
            HomeViewModel(
                obtenerCriaturasUseCase = appContainer.obtenerCriaturasUseCase,
                obtenerMuseosUseCase = appContainer.obtenerMuseosUseCase
            )
        }

        HomeScreen(viewModel = homeViewModel)
    }
}