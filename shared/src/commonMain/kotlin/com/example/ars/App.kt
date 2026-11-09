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

import com.example.ars.features.museos.presentation.MuseosViewModel
import com.example.ars.features.museos.presentation.ui.MuseosScreenRoot

import com.example.ars.features.ajustes.presentation.ui.AjustesScreenRoot

@Composable
fun App(
    appContainer: AppContainer = remember { DefaultAppContainer() },
    toScreen: Route? = null,
    onDestinoAbierto: () -> Unit = {},
    onLeerQr: ((
        onLeido: (String?) -> Unit,
        onCancelado: () -> Unit,
        onError: () -> Unit
    ) -> Unit)? = null,
    onModoOscuroCambiado: (Boolean) -> Unit = {}
) {
    var modoOscuro by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(modoOscuro) {
        onModoOscuroCambiado(modoOscuro)
    }

    ArsTheme(modoOscuro = modoOscuro) {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()

        val homeViewModel = viewModel {
            HomeViewModel(
                obtenerCriaturasUseCase = appContainer.obtenerCriaturasUseCase,
                obtenerMuseosUseCase = appContainer.obtenerCatalogoMuseosUseCase
            )
        }

        val escanerViewModel = if (onLeerQr != null) {
            viewModel {
                EscanerViewModel(
                    validarQrUseCase = appContainer.validarQrUseCase
                )
            }
        } else null

        val selectedItem = when {
            backStackEntry?.destination?.hasRoute<Route.Escaner>() == true ->
                ArsNavigationItem.Escanear

            backStackEntry?.destination?.hasRoute<Route.Museos>() == true ->
                ArsNavigationItem.Museos

            backStackEntry?.destination?.hasRoute<Route.Ajustes>() == true ->
                ArsNavigationItem.Ajustes

            else ->
                ArsNavigationItem.Inicio
        }

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
                        val destino = when (item) {
                            ArsNavigationItem.Inicio -> Route.Home
                            ArsNavigationItem.Museos -> Route.Museos
                            ArsNavigationItem.Escanear -> Route.Escaner
                            ArsNavigationItem.Ajustes -> Route.Ajustes
                            ArsNavigationItem.Colecciones -> null
                        }

                        if (destino != null && item != selectedItem) {
                            navController.navigate(destino) {
                                // cambiamos de sección y dejamos Inicio para volver atrás
                                popUpTo<Route.Home>()
                                launchSingleTop = true
                            }
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
                    composable<Route.Home> {
                        HomeScreenRoot(viewModel = homeViewModel)
                    }

                    composable<Route.Museos> {
                        val museosViewModel = viewModel {
                            MuseosViewModel(
                                obtenerCatalogoMuseosUseCase = appContainer.obtenerCatalogoMuseosUseCase
                            )
                        }
                        MuseosScreenRoot(viewModel = museosViewModel)
                    }

                    composable<Route.Ajustes> {
                        AjustesScreenRoot(
                            modoOscuro = modoOscuro,
                            onModoOscuroCambiado = { modoOscuro = it }
                        )
                    }

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
