package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.presentation.HomeUiState
import com.example.ars.features.museos.presentation.ui.MisionCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.*

@Composable
fun HomeScreen(
    state: HomeUiState,
    onExplorarMuseos: () -> Unit = {},
    onMuseoClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // LOGO ORIGINAL DE ARS
        Image(
            painter = painterResource(Res.drawable.ars_icon_image),
            contentDescription = stringResource(Res.string.home_logo_desc),
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(Res.string.home_bienvenida),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(Res.string.home_subtitulo),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 16.dp
            )
        )

        if (state.cargando) {
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // 1. TU COLECCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.home_ultimas_criaturas_titulo),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(Res.string.home_ultimas_criaturas_subtitulo),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                enabled = false,
                onClick = {}
            ) {
                Text(stringResource(Res.string.home_ver_todas))
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.ultimasCriaturas,
                key = { it.id }
            ) { criatura ->
                CriaturaCard(criatura = criatura)
            }
        }

        if (
            !state.cargando &&
            state.errores.isEmpty() &&
            state.criaturasDescubiertas.isEmpty()
        ) {
            Text(
                text = stringResource(Res.string.home_sin_criaturas),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        // 2. MISIONES EMPEZADAS
        Text(
            text = stringResource(Res.string.home_misiones_titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 12.dp)
        )

        val pendientes = state.museos
            .filter { it.id in state.progreso.museosVisitados }
            .flatMap { museo ->
                museo.progresoMisiones.filterNot { it.completada }.map { museo.nombre to it }
            }
        if (pendientes.isEmpty()) {
            Text(stringResource(Res.string.mision_sin_pendientes))
        }
        pendientes.take(3).forEach { (museo, mision) ->
            Text(museo, style = MaterialTheme.typography.titleMedium)
            MisionCard(mision, state.criaturas)
        }
        TextButton(onClick = onExplorarMuseos) {
            Text(stringResource(Res.string.home_explorar_museos))
        }

        // 3. MUSEOS DESTACADOS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.home_explora_museos_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onExplorarMuseos) {
                Text(stringResource(Res.string.home_ver_todos))
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.museos.filter { it.destacado },
                key = { it.id }
            ) { museo ->
                MuseoCard(
                    museo = museo,
                    desbloqueado = museo.id in state.progreso.museosVisitados,
                    onClick = { onMuseoClick(museo.id) }
                )
            }
        }

        if (state.errores.isNotEmpty()) {
            Text(
                text = state.errores.map { stringResource(it) }.joinToString("\n"),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
