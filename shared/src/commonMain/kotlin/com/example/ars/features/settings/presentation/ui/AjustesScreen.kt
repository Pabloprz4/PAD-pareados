package com.example.ars.features.ajustes.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.core.i18n.LocalIdioma
import ars.shared.generated.resources.*
import com.example.ars.features.settings.presentation.SettingsAction
import com.example.ars.features.settings.presentation.SettingsUiState
import com.example.ars.features.settings.presentation.SettingsViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AjustesScreenRoot(
    viewModel: SettingsViewModel,
    modoOscuro: Boolean,
    onModoOscuroCambiado: (Boolean) -> Unit,
    correoCuenta: String?,
    cuentaCargando: Boolean,
    mensajeCuenta: StringResource?,
    onCuentaClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AjustesScreen(
        state = state,
        onAction = viewModel::onAction,
        modoOscuro = modoOscuro,
        onModoOscuroCambiado = onModoOscuroCambiado,
        correoCuenta = correoCuenta,
        cuentaCargando = cuentaCargando,
        mensajeCuenta = mensajeCuenta,
        onCuentaClick = onCuentaClick
    )
}

@Composable
fun AjustesScreen(
    state: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    modoOscuro: Boolean,
    onModoOscuroCambiado: (Boolean) -> Unit,
    correoCuenta: String?,
    cuentaCargando: Boolean,
    mensajeCuenta: StringResource?,
    onCuentaClick: () -> Unit
) {
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
            text = stringResource(Res.string.ajustes_titulo),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(Res.string.cuenta_titulo), style = MaterialTheme.typography.titleMedium)
                correoCuenta?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                mensajeCuenta?.let {
                    Text(stringResource(it), color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onCuentaClick, enabled = !cuentaCargando) {
                    Text(stringResource(if (correoCuenta == null) Res.string.cuenta_entrar else Res.string.cuenta_salir))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.ajustes_preferencias),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.ajustes_notificaciones),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.ajustes_notificaciones_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // pendiente
                    Switch(
                        checked = state.notificacionesMisiones,
                        onCheckedChange = { onAction(SettingsAction.CambiarNotificaciones(it)) },
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
                        Text(
                            text = stringResource(Res.string.ajustes_tema_oscuro),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.ajustes_tema_oscuro_desc),
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

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.ajustes_idioma),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.ajustes_idioma_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = LocalIdioma.current == "es",
                            onClick = { onAction(SettingsAction.CambiarIdioma("es")) },
                            label = { Text(stringResource(Res.string.ajustes_idioma_es)) }
                        )
                        FilterChip(
                            selected = LocalIdioma.current == "en",
                            onClick = { onAction(SettingsAction.CambiarIdioma("en")) },
                            label = { Text(stringResource(Res.string.ajustes_idioma_en)) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.ajustes_cuenta_titulo),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.nombreUsuario,
                    onValueChange = { onAction(SettingsAction.CambiarNombreUsuario(it)) },
                    label = { Text(stringResource(Res.string.ajustes_nombre_usuario)) },
                    placeholder = { Text(stringResource(Res.string.ajustes_nombre_placeholder)) },
                    supportingText = { Text(stringResource(Res.string.ajustes_nombre_usuario_desc)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.ajustes_acerca_titulo),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.ajustes_version),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(Res.string.ajustes_acerca_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
