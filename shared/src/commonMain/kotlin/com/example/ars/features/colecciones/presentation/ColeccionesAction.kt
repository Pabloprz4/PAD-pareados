package com.example.ars.features.colecciones.presentation

sealed interface ColeccionesAction {

    data object CargarContenido : ColeccionesAction

    data object Reintentar : ColeccionesAction
}