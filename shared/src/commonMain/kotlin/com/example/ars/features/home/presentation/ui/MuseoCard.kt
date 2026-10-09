package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.ars.features.home.domain.model.Museo
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.prado
import ars.shared.generated.resources.rsofia
import ars.shared.generated.resources.tissen

@Composable
fun MuseoCard(
    museo: Museo,
    modifier: Modifier = Modifier
) {

    val progreso = if (museo.obrasTotales > 0) {
        museo.obrasDescubiertas.toFloat() / museo.obrasTotales.toFloat()
    } else {
        0f
    }

    val porcentaje = (progreso * 100).toInt()

    val imagenMuseo = when (museo.nombre) {
        "Museo del Prado" -> Res.drawable.prado
        "Museo Reina Sofía" -> Res.drawable.rsofia
        "Museo Thyssen-Bornemisza" -> Res.drawable.tissen
        else -> Res.drawable.prado
    }

    val estado = if (museo.obrasDescubiertas == 0) {
        "SIN EXPLORAR"
    } else {
        "EN PROGRESO"
    }

    Card(
        modifier = modifier
            .width(360.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column {

            // Imagen principal del museo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {

                Image(
                    painter = painterResource(imagenMuseo),
                    contentDescription = museo.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Degradado para poder leer el texto encima de la imagen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.80f)
                                )
                            )
                        )
                )

                // Estado del museo
                Text(
                    text = estado,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                )

                // Información sobre la fotografía
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(18.dp)
                ) {

                    Text(
                        text = museo.ciudad.uppercase(),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = museo.nombre,
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Información inferior
            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            text = "OBRAS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "${museo.obrasDescubiertas}/${museo.obrasTotales}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "CRIATURAS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "${museo.criaturasDescubiertas}/${museo.criaturasTotales}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "$porcentaje% completado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
