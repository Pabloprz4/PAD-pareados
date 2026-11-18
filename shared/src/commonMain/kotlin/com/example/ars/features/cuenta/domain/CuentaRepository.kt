package com.example.ars.features.cuenta.domain

import kotlinx.coroutines.flow.Flow

sealed interface SesionCuenta {
    data object Cargando : SesionCuenta
    data object SinCuenta : SesionCuenta
    data class Activa(val id: String, val correo: String) : SesionCuenta
}

enum class ErrorCuenta {
    Credenciales, CorreoSinConfirmar, CorreoYaRegistrado, CorreoNoAutorizado, DemasiadosIntentos, Otro
}

class FalloCuenta(val motivo: ErrorCuenta) : Exception()

interface CuentaRepository {
    val sesion: Flow<SesionCuenta>
    suspend fun entrar(correo: String, clave: String)
    // true si Supabase pide confirmar el correo antes de entrar
    suspend fun registrar(correo: String, clave: String): Boolean
    suspend fun salir()
}
