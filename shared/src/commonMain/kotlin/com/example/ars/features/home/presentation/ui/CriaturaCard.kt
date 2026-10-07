package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ars.features.home.domain.model.Criatura
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.ars_icon_image
import ars.shared.generated.resources.menina_fantasmal
import ars.shared.generated.resources.caballo_guernica
import ars.shared.generated.resources.reloj_derretido

@Composable
fun CriaturaCard(
    criatura: Criatura,
    modifier: Modifier = Modifier
) {
//prueba
    val imagenCriatura = when (criatura.nombre) {
        "Menina Fantasmal" -> Res.drawable.menina_fantasmal
        "Caballo Guernica" -> Res.drawable.caballo_guernica
        else -> Res.drawable.reloj_derretido
    }

    Card(
        modifier = modifier.width(230.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {

        Column {

            // Zona de la ilustración
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(imagenCriatura),
                    contentDescription = criatura.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .padding(18.dp),
                    contentScale = ContentScale.Fit
                )

                // Indicador de criatura conseguida
                Text(
                    text = "DESCUBIERTA",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                )
            }

            // Información de la criatura
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = criatura.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = criatura.museo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Criatura ARS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Ver →",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}