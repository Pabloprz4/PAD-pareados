
package com.example.ars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.ars.core.ui.navigation.ArsNavigationBar
import com.example.ars.core.ui.navigation.ArsNavigationItem
import com.example.ars.core.ui.theme.ArsTheme
import com.example.ars.di.AppContainer
import com.example.ars.di.DefaultAppContainer

import com.example.ars.features.escaner.presentation.EscanerViewModel
import com.example.ars.features.escaner.presentation.ui.EscanerScreenRoot

import com.example.ars.features.home.presentation.HomeViewModel
import com.example.ars.features.home.presentation.ui.HomeScreenRoot

import com.example.ars.features.museos.presentation.ui.MuseosScreenRoot

import com.example.ars.features.museos.presentation.MuseosViewModel
import com.example.ars.features.museos.presentation.ui.MuseosScreenRoot

@Composable
fun App(
    appContainer: AppContainer = remember { DefaultAppContainer() },
    toScreen: Route? = null,
    onDestinoAbierto: () -> Unit = {},
    onLeerQr: ((
        onLeido: (String?) -> Unit,
        onCancelado: () -> Unit,
        onError: () -> Unit
    ) -> Unit)? = null
) {
    ArsTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()

        // ViewModel compartido entre Inicio y Museos
        val homeViewModel = viewModel {
            HomeViewModel(
                obtenerCriaturasUseCase =
                    appContainer.obtenerCriaturasUseCase,
                obtenerMuseosUseCase =
                    appContainer.obtenerMuseosUseCase
            )
        }

        // ViewModel del escáner
        val escanerViewModel = if (onLeerQr != null) {
            viewModel {
                EscanerViewModel(
                    validarQrUseCase = appContainer.validarQrUseCase
                )
            }
        } else null

        // Sección seleccionada en la barra inferior
        val selectedItem = when {
            backStackEntry?.destination?.hasRoute<Route.Escaner>() == true ->
                ArsNavigationItem.Escanear

            backStackEntry?.destination?.hasRoute<Route.Museos>() == true ->
                ArsNavigationItem.Museos

            else ->
                ArsNavigationItem.Inicio
        }

        // Navegación desde notificaciones
        LaunchedEffect(toScreen) {
            toScreen?.let { destino ->
                if (destino != Route.Escaner || onLeerQr != null) {
                    navController.navigate(destino) {
                        popUpTo<Route.Home>()
                        launchSingleTop = true
                    }
                }

                onDestinoAbierto()
            }
        }

        Scaffold(
            bottomBar = {
                ArsNavigationBar(
                    selectedItem = selectedItem,
                    mostrarEscaner = onLeerQr != null,
                    onItemSelected = { item ->
                        when (item) {
                            ArsNavigationItem.Inicio -> {
                                navController.popBackStack<Route.Home>(
                                    inclusive = false
                                )
                            }

                            ArsNavigationItem.Museos -> {
                                navController.navigate(Route.Museos) {
                                    launchSingleTop = true
                                }
                            }

                            ArsNavigationItem.Escanear -> {
                                navController.navigate(Route.Escaner) {
                                    launchSingleTop = true
                                }
                            }

                            else -> Unit
                        }
                    }
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

                    // Pantalla de Inicio
                    composable<Route.Home> {
                        HomeScreenRoot(
                            viewModel = homeViewModel
                        )
                    }

                    // Pantalla de Museos
                    composable<Route.Museos> {
                        val museosViewModel = viewModel {
                            MuseosViewModel(
                                obtenerCatalogoMuseosUseCase =
                                    appContainer.obtenerCatalogoMuseosUseCase
                            )
                        }

                        MuseosScreenRoot(
                            viewModel = museosViewModel
                        )
                    }

                    // Pantalla del escáner
                    if (onLeerQr != null && escanerViewModel != null) {
                        composable<Route.Escaner> {
                            EscanerScreenRoot(
                                viewModel = escanerViewModel,
                                onLeerQr = onLeerQr
                            )
                        }
                    }
                }
            }
        }
    }
}
