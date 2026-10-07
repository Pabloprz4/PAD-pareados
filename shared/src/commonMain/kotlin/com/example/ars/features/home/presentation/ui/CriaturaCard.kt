package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.domain.model.Criatura
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.menina_fantasmal
import ars.shared.generated.resources.caballo_guernica
import androidx.compose.foundation.layout.width
import ars.shared.generated.resources.reloj_derretido

@Composable
fun CriaturaCard(
    criatura: Criatura,
    modifier: Modifier = Modifier
) {

    val imagenCriatura = when (criatura.nombre) {
        "Menina Fantasmal" -> Res.drawable.menina_fantasmal
        "Caballo Guernica" -> Res.drawable.caballo_guernica
        else -> Res.drawable.reloj_derretido
    }

    Card(
        modifier = modifier
            .width(180.dp)
            .padding(end = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(imagenCriatura),
                contentDescription = criatura.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = criatura.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = criatura.museo,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}