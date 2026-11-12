package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ars.shared.generated.resources.*
import com.example.ars.features.museos.presentation.MuseosAction
import com.example.ars.features.museos.presentation.MuseosViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetalleObraScreenRoot(
    obraId: String,
    viewModel: MuseosViewModel,
    criaturaBienvenidaId: String?,
    onCerrar: () -> Unit
) {
    val estado by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) { viewModel.onAction(MuseosAction.CargarMuseos) }
    val obra = estado.obras.firstOrNull { it.id == obraId }
    val museo = estado.museos.firstOrNull { it.id == obra?.museoId }

    when {
        estado.cargando -> CircularProgressIndicator(modifier = Modifier.padding(24.dp))
        estado.error != null || obra == null || museo == null -> Column(modifier = Modifier.padding(20.dp)) {
            Text(stringResource(Res.string.obra_error))
            TextButton(onClick = { viewModel.onAction(MuseosAction.CargarMuseos) }) {
                Text(stringResource(Res.string.museo_reintentar))
            }
            TextButton(onClick = onCerrar) { Text(stringResource(Res.string.museo_cerrar)) }
        }
        obra.id !in estado.progreso.obrasEscaneadas || museo.id !in estado.progreso.museosVisitados -> Column(modifier = Modifier.padding(20.dp)) {
            Text(stringResource(Res.string.obra_escanear))
            TextButton(onClick = onCerrar) { Text(stringResource(Res.string.museo_cerrar)) }
        }
        else -> DetalleObraScreen(
            obra = obra,
            museo = museo.nombre,
            bienvenida = estado.criaturas.firstOrNull { it.id == criaturaBienvenidaId }?.nombre,
            onCerrar = onCerrar
        )
    }
}
