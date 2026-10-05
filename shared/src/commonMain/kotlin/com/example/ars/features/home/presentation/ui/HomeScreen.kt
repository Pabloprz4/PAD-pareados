package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.ars_icon_image
import com.example.ars.features.home.presentation.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ars_icon_image),
            contentDescription = "Icono de Ars",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenido a Ars",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onCargarCriaturasClick() },
            enabled = !state.cargando
        ) {
            Text(if (state.cargando) "Cargando..." else "Explorar Criaturas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.criaturas.forEach { criatura ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = criatura.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = criatura.museo, style = MaterialTheme.typography.labelMedium)
                    Text(text = criatura.descripcion, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}