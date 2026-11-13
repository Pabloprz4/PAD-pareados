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
    "obra_reina_ventana" -> Res.drawable.obra_ventana
    "obra_thyssen_caballero" -> Res.drawable.obra_caballero
    "obra_thyssen_hotel" -> Res.drawable.obra_hotel
    "obra_moma_noche" -> Res.drawable.obra_noche_estrellada
    "obra_moma_memoria" -> Res.drawable.obra_memoria
    "obra_vangogh_girasoles" -> Res.drawable.obra_girasoles
    "obra_vangogh_almendro" -> Res.drawable.obra_almendro
    "obra_uffizi_venus" -> Res.drawable.obra_venus
    "obra_uffizi_primavera" -> Res.drawable.obra_primavera
    "obra_munch_grito" -> Res.drawable.obra_grito
    "obra_munch_madonna" -> Res.drawable.obra_madonna
    "obra_vaticano_creacion" -> Res.drawable.obra_creacion_adan
    "obra_vaticano_atenas" -> Res.drawable.obra_escuela_atenas
    "obra_met_ola" -> Res.drawable.obra_gran_ola
    "obra_met_washington" -> Res.drawable.obra_washington
    "obra_louvre_mona_lisa" -> Res.drawable.obra_mona_lisa
    "obra_louvre_libertad" -> Res.drawable.obra_libertad
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
