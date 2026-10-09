package com.example.ars.features.home.presentation

sealed interface HomeAction {
    data object CargarContenido : HomeAction
}
