package com.example.ars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ars.core.ui.navigation.ArsNavigationBar
import com.example.ars.core.ui.navigation.ArsNavigationItem
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
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
        val homeViewModel = viewModel {
            HomeViewModel(
                obtenerCriaturasUseCase = appContainer.obtenerCriaturasUseCase,
                obtenerMuseosUseCase = appContainer.obtenerMuseosUseCase
            )
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
                HomeScreenRoot(viewModel = homeViewModel)
            }
        }
    }
}
