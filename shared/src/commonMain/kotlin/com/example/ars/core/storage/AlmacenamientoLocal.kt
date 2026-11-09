package com.example.ars.core.storage

expect suspend fun leerTextoLocal(
    clave: String
): String?

expect suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
)