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
            text = "Tu aventura empieza aquí",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = "Elige tu primera criatura para comenzar tu colección.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = "Podrás descubrir las demás durante tu aventura.",
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
                        etiqueta = if (seleccionada) "Seleccionada" else "Inicial"
                    )
                }
            }

            Text(
                text = when (criaturaSeleccionada?.id) {
                    "criatura_001" ->
                        "Una compañera misteriosa nacida del arte de Velázquez."

                    "criatura_012" ->
                        "Un misterioso compañero inspirado en El hijo del hombre de René Magritte."

                    "criatura_002" ->
                        "Un compañero cubista inspirado en el Guernica de Pablo Picasso."

                    else ->
                        "Desliza para conocerlas y toca tu favorita."
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
                Text("Reintentar")
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
                        "Guardando…"
                    } else {
                        criaturaSeleccionada?.let {
                            "Elegir a ${it.nombre}"
                        } ?: "Selecciona una criatura"
                    }
                )
            }
        }
    }
}