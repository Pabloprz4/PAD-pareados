package com.example.ars.features.settings.presentation

sealed interface SettingsAction {
    data class CambiarNotificaciones(val activadas: Boolean) : SettingsAction
    data class CambiarIdioma(val nuevoIdioma: String) : SettingsAction
    data class CambiarNombreUsuario(val nuevoNombre: String) : SettingsAction
}
