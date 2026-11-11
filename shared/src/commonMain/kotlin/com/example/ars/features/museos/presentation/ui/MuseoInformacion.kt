package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.museos.domain.model.Museo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun imagenMuseo(museoId: String): DrawableResource = when (museoId) {
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
    else -> Res.drawable.ars_icon_image
}

@Composable
fun MuseoInformacion(museo: Museo, criaturas: List<Criatura>, progreso: ProgresoUsuario) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Image(
            painter = painterResource(imagenMuseo(museo.id)),
            contentDescription = museo.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
        Text("${museo.ciudad}, ${museo.pais}", style = MaterialTheme.typography.titleMedium)
        Text(museo.descripcion)
        Text(stringResource(Res.string.museo_criaturas), style = MaterialTheme.typography.titleMedium)
        val criaturasMuseo = criaturas.filter { it.id in museo.criaturasIds }
        if (criaturasMuseo.isEmpty()) {
            Text(stringResource(Res.string.museo_sin_criaturas))
        }
        criaturasMuseo.forEach { criatura ->
            val estado = if (criatura.id in progreso.criaturasDescubiertas) {
                Res.string.museo_obtenida
            } else {
                Res.string.museo_pendiente
            }
            Text(stringResource(Res.string.museo_estado_criatura, criatura.nombre, stringResource(estado)))
        }
    }
}

@Composable
fun MuseoMisiones(museo: Museo) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(Res.string.museo_misiones_muestra))
        if (museo.misiones.isEmpty()) {
            Text(stringResource(Res.string.museo_sin_misiones))
        }
        museo.misiones.forEach { mision -> Text("• $mision") }
    }
}
