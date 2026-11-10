package com.example.ars.features.home.presentation

sealed interface PrimeraCriaturaAction {

    data object CargarContenido : PrimeraCriaturaAction

    data class SeleccionarCriatura(
        val criaturaId: String
    ) : PrimeraCriaturaAction

    data object ConfirmarEleccion : PrimeraCriaturaAction

    data object Reintentar : PrimeraCriaturaAction
}