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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextButton



@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val state = viewModel.uiState

    // Cargamos los museos cuando entramos en la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarMuseos()
        viewModel.onCargarCriaturasClick()
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

        //Mensaje de bienvenida

        Text(
            text = "Bienvenido a ARS",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Descubre arte. Completa misiones. Colecciona criaturas.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Sección de museos
        Text(
            text = "¿Qué museo quieres explorar?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB8863B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp)
        )

        //Los museos que puedes explorar
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.museos) { museo ->
                MuseoCard(
                    museo = museo
                )
            }
        }

        //Texto de tus criaturas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "Tu colección",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB8863B)
                )

                Text(
                    text = "Descubre criaturas explorando museos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = {
                    // Más adelante abrirá la colección completa
                }
            ) {
                Text("Ver todas →")
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.criaturas) { criatura ->
                CriaturaCard(criatura = criatura)
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