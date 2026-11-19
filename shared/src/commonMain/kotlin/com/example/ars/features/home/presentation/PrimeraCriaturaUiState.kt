package com.example.ars.features.home.presentation

import com.example.ars.features.home.domain.model.Criatura
import org.jetbrains.compose.resources.StringResource

data class PrimeraCriaturaUiState(
    val cargando: Boolean = true,
    val opciones: List<Criatura> = emptyList(),
    val criaturaSeleccionadaId: String? = null,
    val guardando: Boolean = false,
    val eleccionCompletada: Boolean = false,
    val error: StringResource? = null
)