package com.example.ars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ars.core.ui.navigation.ArsNavigationBar
import com.example.ars.core.ui.navigation.ArsNavigationItem
import com.example.ars.core.ui.theme.ArsTheme
import com.example.ars.di.AppContainer
import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.home.presentation.HomeViewModel
import com.example.ars.features.home.presentation.ui.HomeScreenRoot

@Composable
fun App(
    appContainer: AppContainer = remember { DefaultAppContainer() },
    toScreen: Route? = null,
    onDestinoAbierto: () -> Unit = {}
) {
    ArsTheme {
        val navController = rememberNavController()
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

        LaunchedEffect(toScreen) {
            toScreen?.let { destino ->
                selectedTabIndex = ArsNavigationItem.Inicio.ordinal
                navController.navigate(destino) { launchSingleTop = true }
                onDestinoAbierto()
            }
        }
        Scaffold(
            bottomBar = {
                ArsNavigationBar(
                    selectedItem = ArsNavigationItem.entries[selectedTabIndex],
                    onItemSelected = { selectedTabIndex = it.ordinal }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Route.Home
                ) {
                    composable<Route.Home> {
                        val homeViewModel = viewModel {
                            HomeViewModel(
                                obtenerCriaturasUseCase = appContainer.obtenerCriaturasUseCase,
                                obtenerMuseosUseCase = appContainer.obtenerMuseosUseCase
                            )
                        }
                        HomeScreenRoot(viewModel = homeViewModel)
                    }
                }
            }
        }
    }
}
