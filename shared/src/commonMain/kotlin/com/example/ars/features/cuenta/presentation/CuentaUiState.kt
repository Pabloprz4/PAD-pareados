package com.example.ars.features.cuenta.presentation

import com.example.ars.features.cuenta.domain.SesionCuenta
import org.jetbrains.compose.resources.StringResource

data class CuentaUiState(
    val sesion: SesionCuenta = SesionCuenta.Cargando,
    val registro: Boolean = false,
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val mensaje: StringResource? = null,
    val esError: Boolean = false
)
