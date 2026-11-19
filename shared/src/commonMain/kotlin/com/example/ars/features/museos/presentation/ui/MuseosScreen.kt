package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.museos.domain.model.Museo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MuseosScreen(
    museos: List<Museo>,
    museosVisitados: List<String>,
    onVerObras: () -> Unit,
    onMuseoClick: (Museo) -> Unit = {}
) {
    val nacionales = museos.filter { it.pais == "España" }
    val internacionales = museos.filter { it.pais != "España" }

    val descubiertas = museos.sumOf { it.obrasDescubiertas }
    val totales = museos.sumOf { it.obrasTotales }

    val progreso = if (totales > 0) {
        (descubiertas.toFloat() / totales).coerceIn(0f, 1f)
    } else {
        0f
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.ars_icon_image),
                    contentDescription = stringResource(
                        Res.string.museos_logo_desc
                    ),
                    modifier = Modifier.size(90.dp)
                )

                Text(
                    text = stringResource(Res.string.museos_titulo),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(Res.string.museos_subtitulo),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.museos_coleccion_titulo
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(onClick = onVerObras) {
                            Text(
                                text = stringResource(
                                    Res.string.museos_ver_obras
                                )
                            )
                        }
                    }

                    Text(
                        text = stringResource(
                            Res.string.museos_obras_escaneadas,
                            descubiertas,
                            totales
                        ),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(Res.string.museos_nacionales),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = nacionales,
                    key = { it.id }
                ) { museo ->
                    MuseoCatalogoCard(
                        museo = museo,
                        desbloqueado = museo.id in museosVisitados,
                        onClick = {
                            onMuseoClick(museo)
                        }
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(Res.string.museos_internacionales),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = internacionales,
                    key = { it.id }
                ) { museo ->
                    MuseoCatalogoCard(
                        museo = museo,
                        desbloqueado = museo.id in museosVisitados,
                        onClick = {
                            onMuseoClick(museo)
                        }
                    )
                }
            }
        }
    }
}