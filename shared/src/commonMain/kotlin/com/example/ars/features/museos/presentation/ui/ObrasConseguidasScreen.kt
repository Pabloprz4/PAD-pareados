package com.example.ars.features.museos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.presentation.ui.ObraCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObrasConseguidasScreen(
    obras: List<Obra>,
    onVolver: () -> Unit,
    onObraClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text(
                text = stringResource(
                    Res.string.obras_conseguidas_volver
                )
            )
        }

        Text(
            text = stringResource(
                Res.string.obras_conseguidas_titulo
            ),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (obras.isEmpty()) {
            Text(
                text = stringResource(
                    Res.string.obras_conseguidas_vacia
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = obras,
                    key = { it.id }
                ) { obra ->
                    ObraCard(
                        obra = obra,
                        escaneada = true,
                        onClick = {
                            onObraClick(obra.id)
                        }
                    )
                }
            }
        }
    }
}