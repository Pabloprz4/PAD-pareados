package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetalleMuseoScreen(
    nombreMuseo: String,
    onVolver: () -> Unit,
    contenidoObras: @Composable () -> Unit,
    contenidoMisiones: @Composable () -> Unit
) {
    var pestanaSeleccionada by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver a museos")
        }

        Text(
            text = nombreMuseo,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PrimaryTabRow(selectedTabIndex = pestanaSeleccionada) {
            Tab(
                selected = pestanaSeleccionada == 0,
                onClick = { pestanaSeleccionada = 0 },
                text = { Text("Obras") }
            )
            Tab(
                selected = pestanaSeleccionada == 1,
                onClick = { pestanaSeleccionada = 1 },
                text = { Text("Misiones") }
            )
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            if (pestanaSeleccionada == 0) {
                contenidoObras()
            } else {
                contenidoMisiones()
            }
        }
    }
}