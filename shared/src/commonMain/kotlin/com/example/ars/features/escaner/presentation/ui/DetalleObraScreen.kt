package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.escaner.domain.model.Obra
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetalleObraScreen(obra: Obra, museo: String, bienvenida: String?, onCerrar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onCerrar) { Text(stringResource(Res.string.museo_cerrar)) }
        Text(obra.titulo, style = MaterialTheme.typography.headlineSmall)
        Image(
            painter = painterResource(imagenObra(obra.id)),
            contentDescription = obra.titulo,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().height(240.dp)
        )
        Text(obra.autor, style = MaterialTheme.typography.titleMedium)
        Text(museo, style = MaterialTheme.typography.bodyMedium)
        Text(obra.descripcion)
        bienvenida?.let { Text(stringResource(Res.string.museo_bienvenida, it)) }
    }
}
