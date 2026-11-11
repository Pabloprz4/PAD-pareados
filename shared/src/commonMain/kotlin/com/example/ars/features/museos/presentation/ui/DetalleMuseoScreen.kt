package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import ars.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetalleMuseoScreen(
    nombreMuseo: String,
    onVolver: () -> Unit,
    contenidoInformacion: @Composable () -> Unit,
    contenidoObras: @Composable () -> Unit,
    contenidoMisiones: @Composable () -> Unit
) {
    var pestanaSeleccionada by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        TextButton(onClick = onVolver) {
            Text(stringResource(Res.string.museo_cerrar))
        }
        Text(
            text = nombreMuseo,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val pestanas = listOf(
            Res.string.museo_informacion,
            Res.string.museo_obras,
            Res.string.museo_misiones
        )
        PrimaryTabRow(selectedTabIndex = pestanaSeleccionada) {
            pestanas.forEachIndexed { indice, titulo ->
                Tab(
                    selected = pestanaSeleccionada == indice,
                    onClick = { pestanaSeleccionada = indice },
                    text = { Text(stringResource(titulo)) }
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f, fill = false)
                .verticalScroll(rememberScrollState()).padding(top = 16.dp)
        ) {
            when (pestanaSeleccionada) {
                0 -> contenidoInformacion()
                1 -> contenidoObras()
                2 -> contenidoMisiones()
            }
        }
    }
}
