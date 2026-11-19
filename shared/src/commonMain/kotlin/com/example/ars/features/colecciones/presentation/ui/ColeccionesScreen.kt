package com.example.ars.features.colecciones.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ars.features.colecciones.presentation.ColeccionesAction
import com.example.ars.features.colecciones.presentation.ColeccionesUiState
import com.example.ars.features.home.presentation.ui.CriaturaCard

private val ArsCrema = Color(0xFFF7F0E4)
private val ArsPergamino = Color(0xFFE8D8BC)
private val ArsDorado = Color(0xFFB8893F)
private val ArsDoradoClaro = Color(0xFFE8D3A5)
private val ArsMarron = Color(0xFF3D3024)

@Composable
fun ColeccionesScreen(
    state: ColeccionesUiState,
    onAction: (ColeccionesAction) -> Unit
) {
    var filtro by rememberSaveable {
        mutableIntStateOf(0)
    }

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
            .background(ArsCrema)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ArsCrema,
            contentColor = ArsMarron,
            border = BorderStroke(
                width = 1.dp,
                color = ArsDorado.copy(alpha = 0.45f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                ArsPergamino,
                                ArsCrema
                            )
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tus criaturas",
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = ArsMarron
                )

                Text(
                    text = "El arte cobra vida en tu colección.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArsMarron.copy(alpha = 0.75f)
                )

                if (!state.cargando && state.totalCriaturas > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${state.totalDescubiertas} de " +
                                    "${state.totalCriaturas} descubiertas",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ArsMarron,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${(progreso * 100).toInt()} %",
                            fontWeight = FontWeight.Bold,
                            color = ArsMarron
                        )
                    }

                    LinearProgressIndicator(
                        progress = {
                            progreso.coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = ArsDorado,
                        trackColor = ArsDoradoClaro
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
                CircularProgressIndicator(color = ArsDorado)
            }
        } else {
            state.error?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = {
                        onAction(ColeccionesAction.Reintentar)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = ArsMarron
                    )
                ) {
                    Text("Reintentar")
                }
            }

            if (state.criaturas.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Todas", "Descubiertas", "Pendientes")
                        .forEachIndexed { indice, titulo ->
                            FilterChip(
                                selected = filtro == indice,
                                onClick = { filtro = indice },
                                label = { Text(titulo) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = ArsCrema,
                                    labelColor = ArsMarron,
                                    selectedContainerColor = ArsDoradoClaro,
                                    selectedLabelColor = ArsMarron
                                )
                            )
                        }
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 230.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
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
                            if (criatura.id in descubiertasIds) {
                                CriaturaCard(
                                    criatura = criatura,
                                    borde = BorderStroke(
                                        width = 2.dp,
                                        color = ArsDorado
                                    )
                                )
                            } else {
                                CriaturaPendienteCard(
                                    museo = criatura.museo
                                )
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
                                    "¡Has descubierto todas las criaturas!"
                                } else {
                                    "Explora museos para descubrir tu primera criatura."
                                },
                                textAlign = TextAlign.Center,
                                color = ArsMarron,
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
    Surface(
        modifier = Modifier.width(230.dp),
        shape = RoundedCornerShape(24.dp),
        color = ArsCrema,
        contentColor = ArsMarron,
        border = BorderStroke(
            width = 1.dp,
            color = ArsDorado.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ArsPergamino,
                            ArsCrema
                        )
                    )
                )
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
                                ArsDoradoClaro,
                                ArsPergamino
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = ArsCrema,
                    border = BorderStroke(
                        width = 1.dp,
                        color = ArsDorado
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
                        color = ArsMarron
                    )
                }
            }

            Text(
                text = "Un misterio te espera",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = ArsMarron
            )

            Text(
                text = museo,
                style = MaterialTheme.typography.bodyMedium,
                color = ArsMarron.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                minLines = 2
            )

            HorizontalDivider(
                color = ArsDoradoClaro
            )

            Text(
                text = "POR DESCUBRIR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = ArsMarron
            )
        }
    }
}