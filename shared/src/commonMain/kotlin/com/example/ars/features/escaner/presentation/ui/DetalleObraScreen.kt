package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.core.i18n.textoCatalogo
import com.example.ars.features.escaner.domain.model.Obra
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetalleObraScreen(
    obra: Obra,
    museo: String,
    bienvenida: String?,
    recompensas: List<String>,
    onCerrar: () -> Unit
) {
    val titulo = textoCatalogo("${obra.id}_titulo", obra.titulo)
    val colores = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(titulo, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            TextButton(onClick = onCerrar) { Text(stringResource(Res.string.museo_cerrar)) }
        }
        HorizontalDivider()
        Column(
            modifier = Modifier.weight(1f, fill = false)
                .verticalScroll(rememberScrollState()).padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(imagenObra(obra.id)),
                contentDescription = titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().height(240.dp)
                    .clip(MaterialTheme.shapes.large).background(colores.surfaceVariant).padding(8.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(textoCatalogo("${obra.id}_autor", obra.autor), style = MaterialTheme.typography.titleMedium)
                Text(museo, style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
            }
            Text(textoCatalogo("${obra.id}_descripcion", obra.descripcion))
            bienvenida?.let { Text(stringResource(Res.string.museo_bienvenida, it), color = colores.primary) }
            recompensas.forEach { nombre ->
                Text(stringResource(Res.string.mision_premio_nuevo, nombre), color = colores.primary)
            }
        }
    }
}
