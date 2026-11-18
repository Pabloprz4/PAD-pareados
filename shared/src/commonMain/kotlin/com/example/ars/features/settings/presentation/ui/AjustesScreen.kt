package com.example.ars.features.ajustes.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AjustesScreenRoot(
    modoOscuro: Boolean,
    onModoOscuroCambiado: (Boolean) -> Unit
) {
    var notificacionesMisiones by remember { mutableStateOf(true) }
    val coloresInterruptor = if (modoOscuro) {
        SwitchDefaults.colors()
    } else {
        val colorApagado = MaterialTheme.colorScheme.onSurfaceVariant
        SwitchDefaults.colors(
            uncheckedThumbColor = colorApagado,
            uncheckedBorderColor = colorApagado
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preferencias de la app",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Notificaciones de misiones", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Avisar al estar cerca de un museo o criatura",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // pendiente
                    Switch(
                        checked = notificacionesMisiones,
                        onCheckedChange = { notificacionesMisiones = it },
                        colors = coloresInterruptor
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Tema oscuro", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Ajustar apariencia visual",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = onModoOscuroCambiado,
                        colors = coloresInterruptor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Acerca de ARS",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Versión 1.0 - Proyecto PAD",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Exploración de museos y captura de criaturas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
