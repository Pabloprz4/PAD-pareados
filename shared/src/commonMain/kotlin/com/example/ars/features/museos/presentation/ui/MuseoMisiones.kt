package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ProgresoMision
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MisionCard(estado: ProgresoMision, criaturas: List<Criatura>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text(
                    estado.mision.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (estado.completada) {
                    Icon(
                        painterResource(Res.drawable.nav_check),
                        contentDescription = stringResource(Res.string.mision_completada),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text(stringResource(Res.string.mision_progreso, estado.actual, estado.total))
            LinearProgressIndicator(
                progress = { estado.actual.toFloat() / estado.total },
                modifier = Modifier.fillMaxWidth()
            )
            when {
                estado.completada -> Text(stringResource(Res.string.mision_completada))
                !estado.disponible -> Text(stringResource(Res.string.mision_proximamente))
            }
            val recompensa = criaturas.firstOrNull { it.id == estado.mision.recompensaCriaturaId }
            recompensa?.let {
                Text(stringResource(Res.string.mision_recompensa, it.nombre))
            }
        }
    }
}

@Composable
fun MuseoMisiones(museo: Museo, criaturas: List<Criatura>, progreso: ProgresoUsuario) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (museo.progresoMisiones.isEmpty()) {
            Text(stringResource(Res.string.mision_sin_pendientes))
        }
        museo.progresoMisiones.forEach { estado -> MisionCard(estado, criaturas) }
        val conjunta = criaturas.firstOrNull { it.id == museo.recompensaMisionesId }
        when {
            conjunta != null -> {
                val texto = if (conjunta.id in progreso.criaturasDescubiertas) {
                    Res.string.mision_conjunta_obtenida
                } else {
                    Res.string.mision_conjunta
                }
                Text(stringResource(texto, conjunta.nombre))
            }
            museo.misiones.none { it.recompensaCriaturaId != null } ->
                Text(stringResource(Res.string.mision_recompensa_pendiente))
        }
    }
}
