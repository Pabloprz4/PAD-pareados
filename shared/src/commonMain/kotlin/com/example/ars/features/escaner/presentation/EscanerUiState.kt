package com.example.ars.features.escaner.presentation

import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.model.ResultadoRegistroObra
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ResultadoVisitaMuseo

data class EscanerUiState(
    val museoActivo: Museo? = null,
    val contenido: String? = null,
    val resultado: ResultadoValidacionQr? = null,
    val leyendo: Boolean = false,
    val validando: Boolean = false,
    val error: Boolean = false,
    val visitaPorAbrir: ResultadoVisitaMuseo? = null,
    val obraPorAbrir: ResultadoRegistroObra? = null,
    val obraRepetida: Boolean = false
)
