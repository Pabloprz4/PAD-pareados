package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.presentation.PrimeraCriaturaAction
import com.example.ars.features.home.presentation.PrimeraCriaturaUiState
import org.jetbrains.compose.resources.stringResource
import ars.shared.generated.resources.*

@Composable
fun PrimeraCriaturaScreen(
    state: PrimeraCriaturaUiState,
    onAction: (PrimeraCriaturaAction) -> Unit
) {
    val criaturaSeleccionada = state.opciones.firstOrNull {
        it.id == state.criaturaSeleccionadaId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.bienvenida_titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = stringResource(Res.string.bienvenida_subtitulo),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = stringResource(Res.string.bienvenida_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        if (state.cargando) {
            CircularProgressIndicator()
        }

        if (!state.cargando && !state.eleccionCompletada) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.opciones,
                    key = { it.id }
                ) { criatura ->
                    val seleccionada =
                        criatura.id == state.criaturaSeleccionadaId

                    CriaturaCard(
                        criatura = criatura,
                        modifier = Modifier.selectable(
                            selected = seleccionada,
                            enabled = !state.guardando,
                            role = Role.RadioButton,
                            onClick = {
                                onAction(
                                    PrimeraCriaturaAction.SeleccionarCriatura(
                                        criaturaId = criatura.id
                                    )
                                )
                            }
                        ),
                        borde = if (seleccionada) {
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            null
                        },
                        etiqueta = if (seleccionada) stringResource(Res.string.criatura_seleccionada) else stringResource(Res.string.criatura_inicial)
                    )
                }
            }

            Text(
                text = when (criaturaSeleccionada?.id) {
                    "criatura_001" -> stringResource(Res.string.desc_menina)
                    "criatura_012" -> stringResource(Res.string.desc_manzana)
                    "criatura_002" -> stringResource(Res.string.desc_caballo)
                    else -> stringResource(Res.string.desc_desliza)
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        state.error?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            TextButton(
                enabled = !state.cargando && !state.guardando,
                onClick = {
                    onAction(PrimeraCriaturaAction.Reintentar)
                }
            ) {
                Text(stringResource(Res.string.museo_reintentar))
            }
        }

        if (!state.eleccionCompletada) {
            Button(
                enabled = criaturaSeleccionada != null &&
                        !state.cargando &&
                        !state.guardando &&
                        state.error == null,
                onClick = {
                    onAction(PrimeraCriaturaAction.ConfirmarEleccion)
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = if (state.guardando) {
                        stringResource(Res.string.bienvenida_guardando)
                    } else {
                        criaturaSeleccionada?.let {
                            stringResource(Res.string.bienvenida_elegir, it.nombre)
                        } ?: stringResource(Res.string.bienvenida_selecciona)
                    }
                )
            }
        }
    }
}
