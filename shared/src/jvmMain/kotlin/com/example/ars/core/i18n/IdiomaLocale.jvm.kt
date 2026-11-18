package com.example.ars.core.i18n

import java.util.Locale

actual fun cambiarIdiomaLocale(idioma: String) {
    val locale = Locale.forLanguageTag(idioma)
    Locale.setDefault(locale)
}
