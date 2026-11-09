@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.example.ars.core.storage

import kotlin.js.js

private fun leerDelNavegador(clave: String): String? =
    js("localStorage.getItem(clave)")

private fun guardarEnNavegador(
    clave: String,
    contenido: String
): Unit = js("{ localStorage.setItem(clave, contenido); }")

actual suspend fun leerTextoLocal(
    clave: String
): String? {
    return leerDelNavegador(clave)
}

actual suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
) {
    guardarEnNavegador(clave, contenido)
}
