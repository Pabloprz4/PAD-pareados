package com.example.ars.features.settings.presentation

data class SettingsUiState(
    val notificacionesMisiones: Boolean = true,
    val modoOscuro: Boolean = false,
    val idioma: String = "es",
    val nombreUsuario: String = "",
    val cargando: Boolean = false
)
