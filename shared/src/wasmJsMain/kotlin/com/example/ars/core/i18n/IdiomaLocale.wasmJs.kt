package com.example.ars.core.i18n

import kotlin.js.js

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
private fun cambiarIdiomaArs(idioma: String): Unit = js("window.cambiarIdiomaArs(idioma)")

actual fun cambiarIdiomaLocale(idioma: String) {
    cambiarIdiomaArs(idioma)
}
