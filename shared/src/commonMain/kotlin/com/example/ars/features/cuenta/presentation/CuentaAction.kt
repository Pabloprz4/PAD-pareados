package com.example.ars.features.cuenta.presentation

sealed interface CuentaAction {
    data class CambiarCorreo(val texto: String) : CuentaAction
    data class CambiarClave(val texto: String) : CuentaAction
    data object CambiarModo : CuentaAction
    data object Enviar : CuentaAction
    data object Salir : CuentaAction
    data object LimpiarFormulario : CuentaAction
}
