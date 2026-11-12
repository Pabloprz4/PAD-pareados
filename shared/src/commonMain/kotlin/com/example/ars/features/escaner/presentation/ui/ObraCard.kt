package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.escaner.domain.model.Obra
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun imagenObra(obraId: String): DrawableResource = when (obraId) {
    "obra_001" -> Res.drawable.obra_meninas
    "obra_002" -> Res.drawable.obra_jardin
    "obra_003" -> Res.drawable.obra_guernica
    else -> Res.drawable.ars_icon_image
}

@Composable
fun ObraCard(obra: Obra, escaneada: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (escaneada) 1f else 0.7f)
            .clickable(enabled = escaneada, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(imagenObra(obra.id)),
                contentDescription = obra.titulo,
                contentScale = ContentScale.Fit,
                colorFilter = if (escaneada) null else {
                    ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                },
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(obra.titulo, style = MaterialTheme.typography.titleMedium)
                Text(obra.autor, style = MaterialTheme.typography.bodyMedium)
                Text(
                    stringResource(if (escaneada) Res.string.obra_escaneada else Res.string.obra_pendiente),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
