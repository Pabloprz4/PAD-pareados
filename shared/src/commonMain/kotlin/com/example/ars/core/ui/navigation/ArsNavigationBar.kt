package com.example.ars.core.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.nav_ajustes
import ars.shared.generated.resources.nav_batalla
import ars.shared.generated.resources.nav_colecciones
import ars.shared.generated.resources.nav_escanear
import ars.shared.generated.resources.nav_inicio
import ars.shared.generated.resources.nav_house
import ars.shared.generated.resources.nav_images
import ars.shared.generated.resources.nav_scan_line
import ars.shared.generated.resources.nav_settings
import ars.shared.generated.resources.nav_swords
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class ArsNavigationItem(
    val label: StringResource,
    val icon: DrawableResource
) {
    Inicio(Res.string.nav_inicio, Res.drawable.nav_house),
    Batalla(Res.string.nav_batalla, Res.drawable.nav_swords),
    Escanear(Res.string.nav_escanear, Res.drawable.nav_scan_line),
    Colecciones(Res.string.nav_colecciones, Res.drawable.nav_images),
    Ajustes(Res.string.nav_ajustes, Res.drawable.nav_settings)
}

@Composable
fun ArsNavigationBar(
    selectedItem: ArsNavigationItem,
    onItemSelected: (ArsNavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        HorizontalDivider(color = colors.outline)
        NavigationBar(
            containerColor = colors.surface,
            tonalElevation = 0.dp
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                ArsNavigationItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == selectedItem,
                        onClick = { onItemSelected(item) },
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.label),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    letterSpacing = 0.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.primary,
                            selectedTextColor = colors.primary,
                            indicatorColor = colors.primary.copy(alpha = 0.12f),
                            unselectedIconColor = colors.onSurfaceVariant,
                            unselectedTextColor = colors.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
