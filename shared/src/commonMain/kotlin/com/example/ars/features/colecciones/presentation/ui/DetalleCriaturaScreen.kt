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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.ars.core.i18n.textoCatalogo
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.presentation.ui.CriaturaCard

@Composable
fun DetalleCriaturaScreen(
    criatura: Criatura,
    descubierta: Boolean,
    onCerrar: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        color = colores.surface,
        contentColor = colores.onSurface
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
                    text = stringResource(Res.string.museo_cerrar),
                    color = colores.onSurface
                )
            }

            if (descubierta) {
                CriaturaCard(criatura = criatura)

                HorizontalDivider(
                    color = colores.outlineVariant
                )

                Text(
                    text = stringResource(Res.string.criatura_historia),
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = textoCatalogo("${criatura.id}_descripcion", criatura.descripcion).ifBlank {
                        stringResource(Res.string.criatura_historia_pendiente)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.displayLarge,
                    fontFamily = FontFamily.Serif,
                    color = colores.primary
                )

                Text(
                    text = stringResource(Res.string.criatura_desconocida),
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(Res.string.criatura_pista),
                    style = MaterialTheme.typography.titleMedium,
                    color = colores.onSurfaceVariant
                )

                Text(
                    text = when {
                        criatura.id == "criatura_012" -> stringResource(Res.string.criatura_pista_manzana)
                        criatura.museo == "Colección privada" -> stringResource(Res.string.coleccion_privada)
                        else -> criatura.museo
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(Res.string.criatura_pista_museo),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}