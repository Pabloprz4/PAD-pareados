package com.example.ars.features.escaner.presentation

import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.home.domain.model.Museo

data class EscanerUiState(
    val museoActivo: Museo? = null,
    val contenido: String? = null,
    val resultado: ResultadoValidacionQr? = null,
    val leyendo: Boolean = false,
    val validando: Boolean = false,
    val error: Boolean = false
)
