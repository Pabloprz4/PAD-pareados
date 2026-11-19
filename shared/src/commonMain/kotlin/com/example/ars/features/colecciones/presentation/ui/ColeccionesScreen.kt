package com.example.ars.features.colecciones.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.ars.features.colecciones.presentation.ColeccionesAction
import com.example.ars.features.colecciones.presentation.ColeccionesUiState
import com.example.ars.features.home.presentation.ui.CriaturaCard

@Composable
fun ColeccionesScreen(
    state: ColeccionesUiState,
    onAction: (ColeccionesAction) -> Unit,
    onCriaturaClick: (String) -> Unit
) {
    var filtro by rememberSaveable {
        mutableIntStateOf(0)
    }

    val colores = MaterialTheme.colorScheme
    val descubiertasIds = state.progreso.criaturasDescubiertas.toSet()

    val criaturasVisibles = when (filtro) {
        1 -> state.criaturasDescubiertas

        2 -> state.criaturas.filter {
            it.id !in descubiertasIds
        }

        else -> state.criaturas.sortedByDescending {
            it.id in descubiertasIds
        }
    }

    val progreso = if (state.totalCriaturas > 0) {
        state.totalDescubiertas.toFloat() / state.totalCriaturas
    } else {
        0f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = colores.surface,
            contentColor = colores.onSurface,
            border = BorderStroke(
                width = 1.dp,
                color = colores.primary.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                colores.primary.copy(alpha = 0.12f),
                                colores.surface
                            )
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.colecciones_titulo),
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = colores.onSurface
                )

                Text(
                    text = stringResource(Res.string.colecciones_subtitulo),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colores.onSurfaceVariant
                )

                if (!state.cargando && state.totalCriaturas > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.colecciones_progreso, state.totalDescubiertas, state.totalCriaturas),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colores.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = stringResource(Res.string.colecciones_porcentaje, (progreso * 100).toInt()),
                            fontWeight = FontWeight.Bold,
                            color = colores.onSurface
                        )
                    }

                    LinearProgressIndicator(
                        progress = {
                            progreso.coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = colores.primary,
                        trackColor = colores.primary.copy(alpha = 0.15f)
                    )
                }
            }
        }

        if (state.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (state.error) {
                Text(
                    text = stringResource(Res.string.colecciones_error),
                    color = colores.error
                )

                TextButton(
                    onClick = {
                        onAction(ColeccionesAction.Reintentar)
                    }
                ) {
                    Text(stringResource(Res.string.museo_reintentar))
                }
            }

            if (state.criaturas.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(Res.string.colecciones_todas, Res.string.colecciones_descubiertas, Res.string.colecciones_pendientes)
                        .forEachIndexed { indice, titulo ->
                            FilterChip(
                                selected = filtro == indice,
                                onClick = {
                                    filtro = indice
                                },
                                label = {
                                    Text(stringResource(titulo))
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = colores.surface,
                                    labelColor = colores.onSurface,
                                    selectedContainerColor =
                                        colores.primary.copy(alpha = 0.18f),
                                    selectedLabelColor = colores.onSurface
                                )
                            )
                        }
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(
                        minSize = 230.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(
                        items = criaturasVisibles,
                        key = { it.id }
                    ) { criatura ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(230.dp)
                                    .clickable(
                                        role = Role.Button,
                                        onClickLabel = stringResource(Res.string.criatura_ver_ficha),
                                        onClick = {
                                            onCriaturaClick(criatura.id)
                                        }
                                    )
                            ) {
                                if (criatura.id in descubiertasIds) {
                                    CriaturaCard(
                                        criatura = criatura
                                    )
                                } else {
                                    CriaturaPendienteCard(
                                        museo = criatura.museo
                                    )
                                }
                            }
                        }
                    }

                    if (criaturasVisibles.isEmpty()) {
                        item(
                            span = {
                                GridItemSpan(maxLineSpan)
                            }
                        ) {
                            Text(
                                text = if (filtro == 2) {
                                    stringResource(Res.string.colecciones_completa)
                                } else {
                                    stringResource(Res.string.colecciones_vacia)
                                },
                                textAlign = TextAlign.Center,
                                color = colores.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CriaturaPendienteCard(
    museo: String
) {
    val colores = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.width(230.dp),
        shape = RoundedCornerShape(24.dp),
        color = colores.surface,
        contentColor = colores.onSurface,
        border = BorderStroke(
            width = 1.dp,
            color = colores.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colores.surface,
                                colores.surfaceVariant
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = colores.surface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = colores.outlineVariant
                    )
                ) {
                    Text(
                        text = "?",
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 8.dp
                        ),
                        style = MaterialTheme.typography.displayMedium,
                        fontFamily = FontFamily.Serif,
                        color = colores.onSurfaceVariant
                    )
                }
            }

            Text(
                text = stringResource(Res.string.colecciones_misterio),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colores.onSurface
            )

            Text(
                text = if (museo == "Colección privada") stringResource(Res.string.coleccion_privada) else museo,
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurfaceVariant,
                textAlign = TextAlign.Center,
                minLines = 2
            )

            HorizontalDivider(
                color = colores.outlineVariant
            )

            Text(
                text = stringResource(Res.string.colecciones_por_descubrir),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = colores.onSurfaceVariant
            )
        }
    }
}