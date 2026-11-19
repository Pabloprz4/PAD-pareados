package com.example.ars.core.i18n

import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalIdioma = staticCompositionLocalOf { "es" }

expect fun cambiarIdiomaLocale(idioma: String)
