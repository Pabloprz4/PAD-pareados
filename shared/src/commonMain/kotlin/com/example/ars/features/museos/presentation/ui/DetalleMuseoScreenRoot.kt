package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
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
fun DetalleMuseoScreenRoot(
    museoId: String,
    viewModel: MuseosViewModel,
    onVolver: () -> Unit,
    yaVisitado: Boolean? = null,
    criaturaObtenidaId: String? = null
) {
    val estado by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) { viewModel.onAction(MuseosAction.CargarMuseos) }

    val museo = estado.museos.firstOrNull { it.id == museoId }
    when {
        estado.cargando -> CircularProgressIndicator(modifier = Modifier.padding(24.dp))
        museo == null || estado.error != null -> Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(Res.string.museo_error))
            TextButton(onClick = { viewModel.onAction(MuseosAction.CargarMuseos) }) {
                Text(stringResource(Res.string.museo_reintentar))
            }
            TextButton(onClick = onVolver) { Text(stringResource(Res.string.museo_cerrar)) }
        }
        museo.id !in estado.progreso.museosVisitados -> Column(modifier = Modifier.padding(16.dp)) {
            // también comprobamos el acceso si se llega directamente a esta ruta
            Text(stringResource(Res.string.museo_escanear))
            TextButton(onClick = onVolver) { Text(stringResource(Res.string.museo_cerrar)) }
        }
        else -> DetalleMuseoScreen(
            nombreMuseo = museo.nombre,
            onVolver = onVolver,
            contenidoInformacion = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    yaVisitado?.let {
                        Text(stringResource(if (it) Res.string.museo_repetido else Res.string.museo_nuevo))
                    }
                    val bienvenida = estado.criaturas.firstOrNull { it.id == criaturaObtenidaId }
                    bienvenida?.let {
                        Text(stringResource(Res.string.museo_bienvenida, it.nombre))
                    }
                    MuseoInformacion(museo, estado.criaturas, estado.progreso)
                }
            },
            contenidoObras = { Text(stringResource(Res.string.museo_obras_pendientes)) },
            contenidoMisiones = { MuseoMisiones(museo) }
        )
    }
}
