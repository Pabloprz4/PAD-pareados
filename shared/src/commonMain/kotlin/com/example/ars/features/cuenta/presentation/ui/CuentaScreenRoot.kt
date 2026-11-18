package com.example.ars.features.cuenta.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ars.features.cuenta.presentation.CuentaViewModel

@Composable
fun CuentaScreenRoot(viewModel: CuentaViewModel, onInvitado: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CuentaScreen(state = state, onAction = viewModel::onAction, onInvitado = onInvitado)
}
