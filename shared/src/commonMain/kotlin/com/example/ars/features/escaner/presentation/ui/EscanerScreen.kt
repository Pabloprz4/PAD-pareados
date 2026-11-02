package com.example.ars.features.escaner.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.escaner_autor
import ars.shared.generated.resources.escaner_ciudad
import ars.shared.generated.resources.escaner_codigo_invalido
import ars.shared.generated.resources.escaner_contenido
import ars.shared.generated.resources.escaner_datos_muestra
import ars.shared.generated.resources.escaner_error
import ars.shared.generated.resources.escaner_id
import ars.shared.generated.resources.escaner_indicaciones
import ars.shared.generated.resources.escaner_leer_qr
import ars.shared.generated.resources.escaner_leyendo
import ars.shared.generated.resources.escaner_museo
import ars.shared.generated.resources.escaner_museo_activo
import ars.shared.generated.resources.escaner_museo_desconocido
import ars.shared.generated.resources.escaner_museo_identificado
import ars.shared.generated.resources.escaner_museo_incorrecto
import ars.shared.generated.resources.escaner_ninguno
import ars.shared.generated.resources.escaner_obra_desconocida
import ars.shared.generated.resources.escaner_obra_identificada
import ars.shared.generated.resources.escaner_requiere_museo
import ars.shared.generated.resources.escaner_titulo
import ars.shared.generated.resources.escaner_validando
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.presentation.EscanerAction
import com.example.ars.features.escaner.presentation.EscanerUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun EscanerScreen(state: EscanerUiState, onAction: (EscanerAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(Res.string.escaner_titulo), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(Res.string.escaner_indicaciones))
        Text(
            stringResource(
                Res.string.escaner_museo_activo,
                state.museoActivo?.nombre ?: stringResource(Res.string.escaner_ninguno)
            ),
            style = MaterialTheme.typography.titleMedium
        )
        Button(
            onClick = { onAction(EscanerAction.LeerQr) },
            enabled = !state.leyendo && !state.validando
        ) {
            Text(stringResource(Res.string.escaner_leer_qr))
        }
        if (state.leyendo || state.validando) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Text(stringResource(if (state.leyendo) Res.string.escaner_leyendo else Res.string.escaner_validando))
        }
        if (state.error) {
            Text(stringResource(Res.string.escaner_error), color = MaterialTheme.colorScheme.error)
        }
        state.contenido?.let { contenido ->
            HorizontalDivider()
            Text(stringResource(Res.string.escaner_contenido), style = MaterialTheme.typography.titleSmall)
            SelectionContainer { Text(contenido) }
        }
        when (val resultado = state.resultado) {
            is ResultadoValidacionQr.MuseoValido -> {
                Text(stringResource(Res.string.escaner_museo_identificado), style = MaterialTheme.typography.titleLarge)
                Text(resultado.museo.nombre)
                Text(stringResource(Res.string.escaner_id, resultado.museo.id))
                Text(stringResource(Res.string.escaner_ciudad, resultado.museo.ciudad))
            }
            is ResultadoValidacionQr.ObraValida -> {
                Text(stringResource(Res.string.escaner_obra_identificada), style = MaterialTheme.typography.titleLarge)
                Text(resultado.obra.titulo)
                Text(stringResource(Res.string.escaner_id, resultado.obra.id))
                Text(stringResource(Res.string.escaner_autor, resultado.obra.autor))
                Text(stringResource(Res.string.escaner_museo, state.museoActivo?.nombre ?: resultado.obra.museoId))
                Text(resultado.obra.descripcion)
            }
            ResultadoValidacionQr.CodigoInvalido -> Text(stringResource(Res.string.escaner_codigo_invalido))
            ResultadoValidacionQr.MuseoDesconocido -> Text(stringResource(Res.string.escaner_museo_desconocido))
            ResultadoValidacionQr.ObraDesconocida -> Text(stringResource(Res.string.escaner_obra_desconocida))
            ResultadoValidacionQr.RequiereEscanearMuseo -> Text(stringResource(Res.string.escaner_requiere_museo))
            is ResultadoValidacionQr.MuseoIncorrecto -> Text(stringResource(Res.string.escaner_museo_incorrecto, resultado.museoEsperado.nombre))
            null -> Unit
        }
        Text(stringResource(Res.string.escaner_datos_muestra), style = MaterialTheme.typography.bodySmall)
    }
}
