package com.example.ars.features.colecciones.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ars.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.ars.features.colecciones.presentation.ColeccionesAction
import com.example.ars.features.colecciones.presentation.ColeccionesViewModel

@Composable
fun DetalleCriaturaScreenRoot(
    criaturaId: String,
    viewModel: ColeccionesViewModel,
    onCerrar: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(ColeccionesAction.CargarContenido)
    }

    val criatura = state.criaturas.firstOrNull {
        it.id == criaturaId
    }

    when {
        state.cargando -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onCerrar) {
                    Text(stringResource(Res.string.museo_cerrar))
                }

                CircularProgressIndicator()
            }
        }

        state.error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onClick = onCerrar) {
                    Text(stringResource(Res.string.museo_cerrar))
                }

                Text(
                    text = stringResource(Res.string.criatura_ficha_error),
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = {
                        viewModel.onAction(
                            ColeccionesAction.Reintentar
                        )
                    }
                ) {
                    Text(stringResource(Res.string.museo_reintentar))
                }
            }
        }

        criatura == null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onClick = onCerrar) {
                    Text(stringResource(Res.string.museo_cerrar))
                }

                Text(
                    text = stringResource(Res.string.criatura_ficha_no_encontrada)
                )
            }
        }

        else -> {
            DetalleCriaturaScreen(
                criatura = criatura,
                descubierta = criatura.id in
                        state.progreso.criaturasDescubiertas,
                onCerrar = onCerrar
            )
        }
    }
}