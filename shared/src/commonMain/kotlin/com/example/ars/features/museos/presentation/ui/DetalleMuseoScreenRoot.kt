package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.ars.features.museos.presentation.MuseosAction
import com.example.ars.features.museos.presentation.MuseosViewModel

@Composable
fun DetalleMuseoScreenRoot(
    museoId: String,
    viewModel: MuseosViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(MuseosAction.CargarMuseos)
    }

    val museo = estado.museos.firstOrNull { it.id == museoId }

    if (museo != null) {
        DetalleMuseoScreen(
            nombreMuseo = museo.nombre,
            onVolver = onVolver,
            contenidoObras = {
                Text("Las obras de este museo aparecerán aquí.")
            },
            contenidoMisiones = {
                Text("Aún no hay misiones disponibles.")
            }
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TextButton(onClick = onVolver) {
                Text("Volver a museos")
            }

            if (estado.cargando) {
                CircularProgressIndicator()
            } else {
                Text(estado.error ?: "No se ha encontrado el museo.")
            }
        }
    }
}