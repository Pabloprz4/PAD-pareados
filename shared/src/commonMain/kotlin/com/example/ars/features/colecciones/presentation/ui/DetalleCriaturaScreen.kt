package com.example.ars.features.colecciones.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.presentation.ui.CriaturaCard

private val FichaCrema = Color(0xFFF7F0E4)
private val FichaDorado = Color(0xFFB8893F)
private val FichaMarron = Color(0xFF3D3024)

@Composable
fun DetalleCriaturaScreen(
    criatura: Criatura,
    descubierta: Boolean,
    onCerrar: () -> Unit
) {
    Surface(
        color = FichaCrema,
        contentColor = FichaMarron
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = onCerrar,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "Cerrar",
                    color = FichaMarron
                )
            }

            if (descubierta) {
                CriaturaCard(criatura = criatura)

                HorizontalDivider(
                    color = FichaDorado.copy(alpha = 0.4f)
                )

                Text(
                    text = "Su historia",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = criatura.descripcion.ifBlank {
                        "La historia de esta criatura estará disponible próximamente."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.displayLarge,
                    fontFamily = FontFamily.Serif,
                    color = FichaDorado
                )

                Text(
                    text = "Una criatura por descubrir",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tu pista",
                    style = MaterialTheme.typography.titleMedium,
                    color = FichaMarron.copy(alpha = 0.75f)
                )

                Text(
                    text = criatura.museo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Explora este museo y consulta sus misiones " +
                            "para conocer las criaturas que puedes conseguir.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}