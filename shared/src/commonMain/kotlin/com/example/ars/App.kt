package com.example.ars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ars.core.ui.theme.ArsTheme
import com.example.ars.di.AppContainer
import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.home.presentation.HomeViewModel
import com.example.ars.features.home.presentation.ui.HomeScreenRoot

@Composable
fun App(
    appContainer: AppContainer = remember { DefaultAppContainer() }
) {
    ArsTheme {
        val homeViewModel = viewModel {
            HomeViewModel(
                obtenerCriaturasUseCase = appContainer.obtenerCriaturasUseCase,
                obtenerMuseosUseCase = appContainer.obtenerMuseosUseCase
            )
        }
        HomeScreenRoot(viewModel = homeViewModel)
    }
}
