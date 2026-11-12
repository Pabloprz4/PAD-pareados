
package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import org.jetbrains.compose.resources.stringResource
import ars.shared.generated.resources.nav_lock
import ars.shared.generated.resources.museo_bloqueado
import ars.shared.generated.resources.museo_escanear
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.ars.features.museos.domain.model.Museo

import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res


@Composable
fun MuseoCatalogoCard(
    museo: Museo,
    desbloqueado: Boolean,
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



    Card(
        modifier = modifier
            .width(320.dp)
            .alpha(if (desbloqueado) 1f else 0.7f)
            .clickable(enabled = desbloqueado, onClick = onClick),
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
                    painter = painterResource(imagenMuseo(museo.id)),
                    contentDescription = museo.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (desbloqueado) null else {
                        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    }
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

                if (!desbloqueado) {
                    Icon(
                        painter = painterResource(Res.drawable.nav_lock),
                        contentDescription = stringResource(Res.string.museo_bloqueado),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                            .padding(8.dp).size(20.dp)
                    )
                }

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
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = if (desbloqueado) "$porcentaje% de obras escaneadas" else {
                        stringResource(Res.string.museo_escanear)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
