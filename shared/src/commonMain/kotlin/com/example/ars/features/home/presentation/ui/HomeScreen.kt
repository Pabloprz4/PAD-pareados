package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.presentation.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.ars_icon_image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.size

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val state = viewModel.uiState

    // Cargamos los museos cuando entramos en la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarMuseos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Logo de ARS
        Image(
            painter = painterResource(Res.drawable.ars_icon_image),
            contentDescription = "Logo ARS",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 8.dp)
        )

        Text(
            text = "Bienvenido a Ars",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Sección de museos
        Text(
            text = "¿Qué museo quieres explorar?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        state.museos.forEach { museo ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = museo.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = museo.ciudad
                    )

                    Text(
                        text = "Obras: ${museo.obrasDescubiertas}/${museo.obrasTotales}"
                    )

                    Text(
                        text = "Criaturas: ${museo.criaturasDescubiertas}/${museo.criaturasTotales}"
                    )
                }
            }
        }

        // Botón que ya teníamos para cargar criaturas
        Button(
            onClick = {
                viewModel.onCargarCriaturasClick()
            },
            enabled = !state.cargando,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                if (state.cargando) {
                    "Cargando..."
                } else {
                    "Explorar Criaturas"
                }
            )
        }

        // Mostramos las criaturas cargadas
        state.criaturas.forEach { criatura ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = criatura.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = criatura.museo
                    )

                    Text(
                        text = criatura.descripcion
                    )
                }
            }
        }

        // Mostramos un posible error
        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}