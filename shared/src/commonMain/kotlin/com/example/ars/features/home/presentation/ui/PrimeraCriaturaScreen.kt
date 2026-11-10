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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

                    Surface(
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
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (seleccionada) 3.dp else 1.dp,
                            color = if (seleccionada) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CriaturaCard(criatura = criatura)

                            Text(
                                text = if (seleccionada) {
                                    "Seleccionada"
                                } else {
                                    "Toca para elegir"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = when (criaturaSeleccionada?.id) {
                    "criatura_001" ->
                        "Una compañera misteriosa nacida del arte de Velázquez."

                    "criatura_005" ->
                        "Una chispa de energía inspirada en los girasoles de Van Gogh."

                    "criatura_011" ->
                        "La fuerza de una gran ola te acompañará en tu aventura."

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