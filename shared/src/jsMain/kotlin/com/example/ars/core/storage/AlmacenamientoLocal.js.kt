package com.example.ars.core.storage

private external interface AlmacenamientoNavegador {
    fun getItem(clave: String): String?
    fun setItem(clave: String, contenido: String)
}

private external val localStorage: AlmacenamientoNavegador

actual suspend fun leerTextoLocal(
    clave: String
): String? {
    return localStorage.getItem(clave)
}

actual suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
) {
    localStorage.setItem(clave, contenido)
}