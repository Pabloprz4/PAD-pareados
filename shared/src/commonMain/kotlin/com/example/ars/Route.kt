package com.example.ars

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Escaner : Route

    @Serializable
    data object Museos : Route
}
