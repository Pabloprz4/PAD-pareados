package com.example.ars.features.cuenta.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ars.shared.generated.resources.*
import com.example.ars.features.cuenta.presentation.CuentaAction
import com.example.ars.features.cuenta.presentation.CuentaUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun CuentaScreen(
    state: CuentaUiState,
    onAction: (CuentaAction) -> Unit,
    onInvitado: () -> Unit
) {
    val titulo = if (state.registro) Res.string.cuenta_registro else Res.string.cuenta_entrar

    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = stringResource(titulo), style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = state.correo,
                onValueChange = { onAction(CuentaAction.CambiarCorreo(it)) },
                label = { Text(stringResource(Res.string.cuenta_correo)) },
                singleLine = true,
                enabled = !state.cargando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.clave,
                onValueChange = { onAction(CuentaAction.CambiarClave(it)) },
                label = { Text(stringResource(Res.string.cuenta_clave)) },
                singleLine = true,
                enabled = !state.cargando,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAction(CuentaAction.Enviar) }),
                modifier = Modifier.fillMaxWidth()
            )

            state.mensaje?.let {
                Text(
                    text = stringResource(it),
                    color = if (state.esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
            if (state.cargando) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { onAction(CuentaAction.Enviar) },
                enabled = !state.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(titulo))
            }
            TextButton(
                onClick = { onAction(CuentaAction.CambiarModo) },
                enabled = !state.cargando,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(if (state.registro) Res.string.cuenta_ya_tengo else Res.string.cuenta_registro))
            }
            TextButton(
                onClick = onInvitado,
                enabled = !state.cargando,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(Res.string.cuenta_invitado))
            }
        }
    }
}
