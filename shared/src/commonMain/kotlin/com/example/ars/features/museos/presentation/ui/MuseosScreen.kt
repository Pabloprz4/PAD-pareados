
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.presentation.ui.MuseoCard
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.ars_icon_image

@Composable
fun MuseosScreen(
    museos: List<Museo>,
    onMuseoClick: (Museo) -> Unit = {}
) {
    val nacionales = museos.filter {
        it.pais == "España"
    }

    val internacionales = museos.filter {
        it.pais != "España"
    }

    val obrasDescubiertas = museos.sumOf { it.obrasDescubiertas }
    val obrasTotales = museos.sumOf { it.obrasTotales }

    val progreso = if (obrasTotales > 0) {
        obrasDescubiertas.toFloat() / obrasTotales
    } else {
        0f
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5EF)),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        Res.drawable.ars_icon_image
                    ),
                    contentDescription = "Logo Ars",
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "Tu pasaporte cultural",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Explora museos de todo el mundo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Tu colección de arte",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$obrasDescubiertas / $obrasTotales obras descubiertas",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFFB78643),
                        trackColor = Color(0xFFE9DFD2)
                    )
                }
            }
        }

        item {
            Text(
                text = "Museos nacionales",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(nacionales) { museo ->
                    MuseoCard(
                        museo = museo,
                        onClick = { onMuseoClick(museo) }
                    )
                }
            }
        }

        item {
            Text(
                text = "Museos internacionales",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(internacionales) { museo ->
                    MuseoCard(
                        museo = museo,
                        onClick = { onMuseoClick(museo) }
                    )
                }
            }
        }
    }
}
