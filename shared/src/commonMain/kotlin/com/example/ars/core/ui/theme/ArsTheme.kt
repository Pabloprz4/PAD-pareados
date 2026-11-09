package com.example.ars.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ArsColorScheme = lightColorScheme(
    primary = ArsGold,
    onPrimary = ArsSurface,

    secondary = ArsGoldDark,
    onSecondary = ArsSurface,

    background = ArsBackground,
    onBackground = ArsTextPrimary,

    surface = ArsSurface,
    onSurface = ArsTextPrimary,

    surfaceVariant = ArsSurfaceSoft,
    onSurfaceVariant = ArsTextSecondary,

    outline = ArsBorder
)

private val ArsDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE8B66E),
    onPrimary = Color(0xFF392608),
    background = Color(0xFF1C1915),
    onBackground = Color(0xFFF3EEE5),
    surface = Color(0xFF25211C),
    onSurface = Color(0xFFF3EEE5),
    surfaceVariant = Color(0xFF373027),
    onSurfaceVariant = Color(0xFFD8CDBD),
    surfaceContainerHighest = Color(0xFF373027),
    outline = Color(0xFF75695B)
)

@Composable
fun ArsTheme(
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) ArsDarkColorScheme else ArsColorScheme,
        content = content
    )
}
