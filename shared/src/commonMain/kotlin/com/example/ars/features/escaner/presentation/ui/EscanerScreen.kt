package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.obra_repetida
import ars.shared.generated.resources.obra_repetida_texto
import ars.shared.generated.resources.museo_cerrar
import ars.shared.generated.resources.escaner_autor
import ars.shared.generated.resources.escaner_ciudad
import ars.shared.generated.resources.escaner_codigo_invalido
import ars.shared.generated.resources.escaner_contenido
import ars.shared.generated.resources.escaner_error
import ars.shared.generated.resources.escaner_id
import ars.shared.generated.resources.escaner_indicaciones
import ars.shared.generated.resources.escaner_leer_qr
import ars.shared.generated.resources.escaner_leyendo
import ars.shared.generated.resources.escaner_museo
import ars.shared.generated.resources.escaner_museo_activo
import ars.shared.generated.resources.escaner_museo_desconocido
import ars.shared.generated.resources.escaner_museo_identificado
import ars.shared.generated.resources.nav_scan_line
import ars.shared.generated.resources.escaner_obra_desconocida
import ars.shared.generated.resources.escaner_obra_identificada
import ars.shared.generated.resources.escaner_titulo
import ars.shared.generated.resources.escaner_validando
import com.example.ars.core.i18n.textoCatalogo
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.presentation.EscanerAction
import com.example.ars.features.escaner.presentation.EscanerUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EscanerScreen(state: EscanerUiState, onAction: (EscanerAction) -> Unit) {
    if (state.obraRepetida) {
        AlertDialog(
            onDismissRequest = { onAction(EscanerAction.AvisoCerrado) },
            title = { Text(stringResource(Res.string.obra_repetida)) },
            text = { Text(stringResource(Res.string.obra_repetida_texto)) },
            confirmButton = {
                TextButton(onClick = { onAction(EscanerAction.AvisoCerrado) }) {
                    Text(stringResource(Res.string.museo_cerrar))
                }
            }
        )
    }
    val colores = MaterialTheme.colorScheme
    val ocupado = state.leyendo || state.validando
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.widthIn(max = 540.dp).fillMaxSize()
                .verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = colores.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(shape = CircleShape, color = colores.surface) {
                        Icon(
                            painter = painterResource(Res.drawable.nav_scan_line),
                            contentDescription = null,
                            tint = colores.primary,
                            modifier = Modifier.padding(14.dp).size(28.dp)
                        )
                    }
                    Text(stringResource(Res.string.escaner_titulo), style = MaterialTheme.typography.headlineMedium)
                    Text(
                        stringResource(Res.string.escaner_indicaciones),
                        color = colores.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { onAction(EscanerAction.LeerQr) },
                        enabled = !ocupado,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        if (ocupado) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = LocalContentColor.current,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        val etiqueta = when {
                            state.leyendo -> Res.string.escaner_leyendo
                            state.validando -> Res.string.escaner_validando
                            else -> Res.string.escaner_leer_qr
                        }
                        Text(stringResource(etiqueta))
                    }
                }
            }
            state.museoActivo?.let { museo ->
                Text(
                    stringResource(Res.string.escaner_museo_activo, museo.nombre),
                    style = MaterialTheme.typography.labelLarge,
                    color = colores.onSurfaceVariant
                )
            }
            if (state.error || state.resultado != null || state.contenido != null) {
                ResultadoEscaner(state)
            }
        }
    }
}

@Composable
private fun ResultadoEscaner(state: EscanerUiState) {
    val colores = MaterialTheme.colorScheme
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (state.error) {
                Text(stringResource(Res.string.escaner_error), color = colores.error)
            }
            when (val resultado = state.resultado) {
                is ResultadoValidacionQr.MuseoValido -> {
                    Text(stringResource(Res.string.escaner_museo_identificado), style = MaterialTheme.typography.labelLarge, color = colores.onSurfaceVariant)
                    Text(resultado.museo.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(stringResource(Res.string.escaner_id, resultado.museo.id))
                    val ciudad = textoCatalogo("${resultado.museo.id}_ciudad", resultado.museo.ciudad)
                    Text(stringResource(Res.string.escaner_ciudad, ciudad))
                }
                is ResultadoValidacionQr.ObraValida -> {
                    Text(stringResource(Res.string.escaner_obra_identificada), style = MaterialTheme.typography.labelLarge, color = colores.onSurfaceVariant)
                    val obra = resultado.obra
                    val autor = textoCatalogo("${obra.id}_autor", obra.autor)
                    Text(textoCatalogo("${obra.id}_titulo", obra.titulo), style = MaterialTheme.typography.titleLarge)
                    Text(stringResource(Res.string.escaner_id, obra.id))
                    Text(stringResource(Res.string.escaner_autor, autor))
                    Text(stringResource(Res.string.escaner_museo, resultado.museo.nombre))
                    Text(textoCatalogo("${obra.id}_descripcion", obra.descripcion))
                }
                ResultadoValidacionQr.CodigoInvalido -> Text(stringResource(Res.string.escaner_codigo_invalido), color = colores.error)
                ResultadoValidacionQr.MuseoDesconocido -> Text(stringResource(Res.string.escaner_museo_desconocido), color = colores.error)
                ResultadoValidacionQr.ObraDesconocida -> Text(stringResource(Res.string.escaner_obra_desconocida), color = colores.error)
                null -> Unit
            }
            state.contenido?.let { contenido ->
                if (state.resultado != null || state.error) HorizontalDivider()
                Text(
                    stringResource(Res.string.escaner_contenido),
                    style = MaterialTheme.typography.labelMedium,
                    color = colores.onSurfaceVariant
                )
                SelectionContainer {
                    Text(contenido, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
