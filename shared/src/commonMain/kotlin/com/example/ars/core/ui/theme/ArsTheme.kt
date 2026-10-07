package com.example.ars.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun ArsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ArsColorScheme,
        content = content
    )
}