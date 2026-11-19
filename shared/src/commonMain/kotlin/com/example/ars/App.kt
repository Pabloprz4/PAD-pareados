package com.example.ars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ars.core.i18n.LocalIdioma
import com.example.ars.core.i18n.cambiarIdiomaLocale
import com.example.ars.core.ui.navigation.ArsNavigationBar
import com.example.ars.core.ui.navigation.ArsNavigationItem
import com.example.ars.core.ui.theme.ArsTheme
import com.example.ars.di.AppContainer
import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.ajustes.presentation.ui.AjustesScreenRoot
import com.example.ars.features.colecciones.presentation.ColeccionesViewModel
import com.example.ars.features.colecciones.presentation.ui.ColeccionesScreenRoot
import com.example.ars.features.colecciones.presentation.ui.DetalleCriaturaScreenRoot
import com.example.ars.features.cuenta.domain.SesionCuenta
import com.example.ars.features.cuenta.presentation.CuentaAction
import com.example.ars.features.cuenta.presentation.CuentaViewModel
import com.example.ars.features.cuenta.presentation.ui.CuentaScreenRoot
import com.example.ars.features.escaner.presentation.EscanerViewModel
import com.example.ars.features.escaner.presentation.ui.DetalleObraScreenRoot
import com.example.ars.features.escaner.presentation.ui.EscanerScreenRoot
import com.example.ars.features.home.presentation.HomeViewModel
import com.example.ars.features.home.presentation.PrimeraCriaturaViewModel
import com.example.ars.features.home.presentation.ui.HomeScreenRoot
import com.example.ars.features.home.presentation.ui.PrimeraCriaturaScreenRoot
import com.example.ars.features.museos.presentation.MuseosViewModel
import com.example.ars.features.museos.presentation.ui.DetalleMuseoScreenRoot
import com.example.ars.features.museos.presentation.ui.MuseosScreenRoot
import com.example.ars.features.museos.presentation.ui.ObrasConseguidasScreenRoot
import com.example.ars.features.settings.presentation.SettingsAction
import com.example.ars.features.settings.presentation.SettingsViewModel

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
    onModoOscuroCambiado: (Boolean) -> Unit = {},
    onIdiomaCambiado: (String) -> Unit = {}
) {
    val settingsViewModel: SettingsViewModel = viewModel {
        SettingsViewModel()
    }
    val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()

    val modoOscuro = settingsState.modoOscuro

    var idiomaAplicado by remember {
        mutableStateOf<String?>(null)
    }

    val idiomaPreferido = settingsState.idioma
    LaunchedEffect(idiomaPreferido, settingsState.cargando) {
        if (!settingsState.cargando) {
            onIdiomaCambiado(idiomaPreferido)
            cambiarIdiomaLocale(idiomaPreferido)
            idiomaAplicado = idiomaPreferido
        }
    }

    LaunchedEffect(modoOscuro) {
        onModoOscuroCambiado(modoOscuro)
    }

    val cuentaViewModel: CuentaViewModel = viewModel {
        CuentaViewModel(appContainer.cuentaRepository)
    }
    val estadoCuenta by cuentaViewModel.state.collectAsStateWithLifecycle()

    val sesion = estadoCuenta.sesion
    val cuenta = sesion as? SesionCuenta.Activa
    val idioma = idiomaAplicado

    ArsTheme(modoOscuro = modoOscuro) {
        if (sesion == SesionCuenta.Cargando || idioma == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Cada cuenta utiliza su navegación y sus ViewModels.
            key(cuenta?.id) {
                val contenedor = remember(appContainer, cuenta?.id) {
                    cuenta?.let {
                        appContainer.paraCuenta(it.id)
                    } ?: appContainer
                }

                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides rememberViewModelStoreOwner()
                ) {
                    NavegacionArs(
                        idioma = idioma,
                        appContainer = contenedor,
                        cuentaViewModel = cuentaViewModel,
                        cuenta = cuenta,
                        settingsViewModel = settingsViewModel,
                        modoOscuro = modoOscuro,
                        onModoOscuroCambiado = {
                            settingsViewModel.onAction(
                                SettingsAction.CambiarTemaOscuro(it)
                            )
                        },
                        toScreen = toScreen,
                        onDestinoAbierto = onDestinoAbierto,
                        onLeerQr = onLeerQr
                    )
                }
            }
        }
    }
}

@Composable
private fun NavegacionArs(
    idioma: String,
    appContainer: AppContainer,
    cuentaViewModel: CuentaViewModel,
    cuenta: SesionCuenta.Activa?,
    settingsViewModel: SettingsViewModel,
    modoOscuro: Boolean,
    onModoOscuroCambiado: (Boolean) -> Unit,
    toScreen: Route?,
    onDestinoAbierto: () -> Unit,
    onLeerQr: (((String?) -> Unit, () -> Unit, () -> Unit) -> Unit)?
) {
    val estadoCuenta by cuentaViewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    CompositionLocalProvider(LocalIdioma provides idioma) {
        // Actualiza los textos al cambiar el idioma conservando la ruta.
        key(idioma) {
            val backStackEntry by navController.currentBackStackEntryAsState()

            val mostrandoBienvenida =
                backStackEntry == null ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.PrimeraCriatura>() == true ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.Cuenta>() == true

            val homeViewModel = viewModel {
                HomeViewModel(
                    obtenerCriaturasUseCase =
                        appContainer.obtenerCriaturasUseCase,
                    obtenerMuseosUseCase =
                        appContainer.obtenerCatalogoMuseosUseCase,
                    obtenerProgresoUseCase =
                        appContainer.obtenerProgresoUseCase
                )
            }

            val escanerViewModel = if (onLeerQr != null) {
                viewModel {
                    EscanerViewModel(
                        validarQrUseCase =
                            appContainer.validarQrUseCase,
                        registrarVisitaMuseoUseCase =
                            appContainer.registrarVisitaMuseoUseCase,
                        registrarObraUseCase =
                            appContainer.registrarObraUseCase
                    )
                }
            } else {
                null
            }

            val selectedItem = when {
                backStackEntry?.destination
                    ?.hasRoute<Route.Escaner>() == true ->
                    ArsNavigationItem.Escanear

                backStackEntry?.destination
                    ?.hasRoute<Route.Museos>() == true ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.DetalleMuseo>() == true ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.DetalleObra>() == true ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.ObrasConseguidas>() == true ->
                    ArsNavigationItem.Museos

                backStackEntry?.destination
                    ?.hasRoute<Route.Ajustes>() == true ->
                    ArsNavigationItem.Ajustes

                backStackEntry?.destination
                    ?.hasRoute<Route.Colecciones>() == true ||
                        backStackEntry?.destination
                            ?.hasRoute<Route.DetalleCriatura>() == true ->
                    ArsNavigationItem.Colecciones

                else ->
                    ArsNavigationItem.Inicio
            }

            // Espera a terminar la bienvenida antes de abrir una notificación.
            LaunchedEffect(toScreen, mostrandoBienvenida) {
                if (!mostrandoBienvenida) {
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
            }

            Scaffold(
                bottomBar = {
                    if (!mostrandoBienvenida) {
                        ArsNavigationBar(
                            selectedItem = selectedItem,
                            mostrarEscaner = onLeerQr != null,
                            onItemSelected = { item ->
                                val destino = when (item) {
                                    ArsNavigationItem.Inicio ->
                                        Route.Home

                                    ArsNavigationItem.Museos ->
                                        Route.Museos

                                    ArsNavigationItem.Escanear ->
                                        Route.Escaner

                                    ArsNavigationItem.Ajustes ->
                                        Route.Ajustes

                                    ArsNavigationItem.Colecciones ->
                                        Route.Colecciones
                                }

                                if (item != selectedItem) {
                                    navController.navigate(destino) {
                                        popUpTo<Route.Home>()
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
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
                        startDestination = if (cuenta == null) {
                            Route.Cuenta
                        } else {
                            Route.PrimeraCriatura
                        }
                    ) {
                        composable<Route.Cuenta> {
                            CuentaScreenRoot(
                                viewModel = cuentaViewModel,
                                onInvitado = {
                                    cuentaViewModel.onAction(
                                        CuentaAction.LimpiarFormulario
                                    )

                                    if (
                                        navController.previousBackStackEntry != null
                                    ) {
                                        navController.popBackStack()
                                    } else {
                                        navController.navigate(
                                            Route.PrimeraCriatura
                                        ) {
                                            popUpTo<Route.Cuenta> {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable<Route.PrimeraCriatura> {
                            val primeraCriaturaViewModel:
                                    PrimeraCriaturaViewModel = viewModel {
                                PrimeraCriaturaViewModel(
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase,
                                    elegirCriaturaInicialUseCase =
                                        appContainer.elegirCriaturaInicialUseCase
                                )
                            }

                            PrimeraCriaturaScreenRoot(
                                viewModel = primeraCriaturaViewModel,
                                onEleccionCompletada = {
                                    navController.navigate(Route.Home) {
                                        popUpTo<Route.PrimeraCriatura> {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Route.Home> {
                            HomeScreenRoot(
                                viewModel = homeViewModel,
                                onVerCriaturas = {
                                    navController.navigate(Route.Colecciones) {
                                        launchSingleTop = true
                                    }
                                },
                                onExplorarMuseos = {
                                    navController.navigate(Route.Museos) {
                                        launchSingleTop = true
                                    }
                                },
                                onMuseoClick = { museoId ->
                                    navController.navigate(
                                        Route.DetalleMuseo(museoId)
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Route.Museos> {
                            val museosViewModel = viewModel {
                                MuseosViewModel(
                                    obtenerCatalogoMuseosUseCase =
                                        appContainer.obtenerCatalogoMuseosUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase,
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerObrasUseCase =
                                        appContainer.obtenerObrasUseCase
                                )
                            }

                            MuseosScreenRoot(
                                viewModel = museosViewModel,
                                onVerObras = {
                                    navController.navigate(
                                        Route.ObrasConseguidas
                                    ) {
                                        launchSingleTop = true
                                    }
                                },
                                onMuseoClick = { museo ->
                                    navController.navigate(
                                        Route.DetalleMuseo(
                                            museoId = museo.id
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Route.ObrasConseguidas> {
                            val obrasViewModel: MuseosViewModel = viewModel {
                                MuseosViewModel(
                                    obtenerCatalogoMuseosUseCase =
                                        appContainer.obtenerCatalogoMuseosUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase,
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerObrasUseCase =
                                        appContainer.obtenerObrasUseCase
                                )
                            }

                            ObrasConseguidasScreenRoot(
                                viewModel = obrasViewModel,
                                onVolver = {
                                    navController.popBackStack()
                                },
                                onObraClick = { obraId ->
                                    navController.navigate(
                                        Route.DetalleObra(
                                            obraId = obraId
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Route.Colecciones> {
                            val coleccionesViewModel:
                                    ColeccionesViewModel = viewModel {
                                ColeccionesViewModel(
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase
                                )
                            }

                            ColeccionesScreenRoot(
                                viewModel = coleccionesViewModel,
                                onCriaturaClick = { criaturaId ->
                                    navController.navigate(
                                        Route.DetalleCriatura(criaturaId)
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        dialog<Route.DetalleMuseo> { entrada ->
                            val ruta =
                                entrada.toRoute<Route.DetalleMuseo>()

                            val detalleMuseoViewModel = viewModel {
                                MuseosViewModel(
                                    obtenerCatalogoMuseosUseCase =
                                        appContainer.obtenerCatalogoMuseosUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase,
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerObrasUseCase =
                                        appContainer.obtenerObrasUseCase
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 680.dp)
                            ) {
                                DetalleMuseoScreenRoot(
                                    museoId = ruta.museoId,
                                    viewModel = detalleMuseoViewModel,
                                    onVolver = {
                                        navController.popBackStack()
                                    },
                                    onObraClick = { obraId ->
                                        navController.navigate(
                                            Route.DetalleObra(obraId)
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    yaVisitado = ruta.yaVisitado,
                                    criaturaObtenidaId =
                                        ruta.criaturaObtenidaId,
                                    recompensasIds = ruta.recompensasIds
                                )
                            }
                        }

                        dialog<Route.DetalleObra> { entrada ->
                            val ruta =
                                entrada.toRoute<Route.DetalleObra>()

                            val obraViewModel = viewModel {
                                MuseosViewModel(
                                    obtenerCatalogoMuseosUseCase =
                                        appContainer.obtenerCatalogoMuseosUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase,
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerObrasUseCase =
                                        appContainer.obtenerObrasUseCase
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 680.dp)
                            ) {
                                DetalleObraScreenRoot(
                                    obraId = ruta.obraId,
                                    viewModel = obraViewModel,
                                    criaturaBienvenidaId =
                                        ruta.criaturaBienvenidaId,
                                    recompensasIds = ruta.recompensasIds,
                                    onCerrar = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        dialog<Route.DetalleCriatura> { entrada ->
                            val ruta =
                                entrada.toRoute<Route.DetalleCriatura>()

                            val detalleCriaturaViewModel:
                                    ColeccionesViewModel = viewModel {
                                ColeccionesViewModel(
                                    obtenerCriaturasUseCase =
                                        appContainer.obtenerCriaturasUseCase,
                                    obtenerProgresoUseCase =
                                        appContainer.obtenerProgresoUseCase
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 680.dp)
                            ) {
                                DetalleCriaturaScreenRoot(
                                    criaturaId = ruta.criaturaId,
                                    viewModel = detalleCriaturaViewModel,
                                    onCerrar = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        composable<Route.Ajustes> {
                            AjustesScreenRoot(
                                viewModel = settingsViewModel,
                                modoOscuro = modoOscuro,
                                onModoOscuroCambiado =
                                    onModoOscuroCambiado,
                                correoCuenta = cuenta?.correo,
                                cuentaCargando = estadoCuenta.cargando,
                                mensajeCuenta = estadoCuenta.mensaje,
                                onCuentaClick = {
                                    if (cuenta == null) {
                                        cuentaViewModel.onAction(
                                            CuentaAction.LimpiarFormulario
                                        )

                                        navController.navigate(Route.Cuenta) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        cuentaViewModel.onAction(
                                            CuentaAction.Salir
                                        )
                                    }
                                }
                            )
                        }

                        if (
                            onLeerQr != null &&
                            escanerViewModel != null
                        ) {
                            composable<Route.Escaner> {
                                EscanerScreenRoot(
                                    viewModel = escanerViewModel,
                                    onLeerQr = onLeerQr,
                                    onMuseoEscaneado = { visita ->
                                        navController.navigate(
                                            Route.DetalleMuseo(
                                                museoId = visita.museoId,
                                                yaVisitado = visita.yaVisitado,
                                                criaturaObtenidaId =
                                                    visita.criaturaObtenidaId,
                                                recompensasIds =
                                                    visita.recompensasIds
                                            )
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onObraEscaneada = { registro ->
                                        navController.navigate(
                                            Route.DetalleObra(
                                                obraId = registro.obraId,
                                                criaturaBienvenidaId =
                                                    registro.criaturaBienvenidaId,
                                                recompensasIds =
                                                    registro.recompensasIds
                                            )
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}