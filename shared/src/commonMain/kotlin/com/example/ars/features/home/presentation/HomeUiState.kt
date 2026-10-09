package com.example.ars.features.home.presentation

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.Museo

data class HomeUiState(
    val cargando: Boolean = false,
    val museos: List<Museo> = emptyList(),
    val criaturas: List<Criatura> = emptyList(),
    val error: String? = null
)
