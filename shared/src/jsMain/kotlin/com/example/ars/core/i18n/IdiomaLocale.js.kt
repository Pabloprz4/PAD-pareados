package com.example.ars.core.i18n

private external fun cambiarIdiomaArs(idioma: String)

actual fun cambiarIdiomaLocale(idioma: String) {
    cambiarIdiomaArs(idioma)
}
