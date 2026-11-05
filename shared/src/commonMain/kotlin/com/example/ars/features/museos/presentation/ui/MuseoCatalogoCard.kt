
package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.ars.features.museos.domain.model.Museo

import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.prado
import ars.shared.generated.resources.rsofia
import ars.shared.generated.resources.tissen
import ars.shared.generated.resources.moma
import ars.shared.generated.resources.vangogh
import ars.shared.generated.resources.uffizi
import ars.shared.generated.resources.munch
import ars.shared.generated.resources.vaticano
import ars.shared.generated.resources.met
import ars.shared.generated.resources.louvre


@Composable
fun MuseoCatalogoCard(
    museo: Museo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val progreso = if (museo.obrasTotales > 0) {
        (museo.obrasDescubiertas.toFloat() / museo.obrasTotales)
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    val porcentaje = (progreso * 100).toInt()

    // Fotografías disponibles actualmente en Ars.
    // Añadiremos las de los museos internacionales después.

    val imagen = when (museo.id) {
        "museo_001" -> Res.drawable.prado
        "museo_002" -> Res.drawable.rsofia
        "museo_003" -> Res.drawable.tissen
        "museo_004" -> Res.drawable.moma
        "museo_005" -> Res.drawable.vangogh
        "museo_006" -> Res.drawable.uffizi
        "museo_007" -> Res.drawable.munch
        "museo_008" -> Res.drawable.vaticano
        "museo_009" -> Res.drawable.met
        "museo_010" -> Res.drawable.louvre
        else -> Res.drawable.prado
    }


    Card(
        modifier = modifier
            .width(320.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                Image(
                    painter = painterResource(imagen),
                    contentDescription = museo.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = museo.ciudad.uppercase(),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = museo.nombre,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "OBRAS",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = "${museo.obrasDescubiertas}/${museo.obrasTotales}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "CRIATURAS",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = "${museo.criaturasDescubiertas}/${museo.criaturasTotales}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFFB78643),
                    trackColor = Color(0xFFE9DFD2)
                )

                Text(
                    text = "$porcentaje% completado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
