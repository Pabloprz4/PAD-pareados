package com.example.ars.features.escaner.domain.model

import com.example.ars.features.museos.domain.model.Museo

sealed interface ResultadoValidacionQr {
    data class MuseoValido(val museo: Museo) : ResultadoValidacionQr
    data class ObraValida(val obra: Obra) : ResultadoValidacionQr
    data object CodigoInvalido : ResultadoValidacionQr
    data object MuseoDesconocido : ResultadoValidacionQr
    data object ObraDesconocida : ResultadoValidacionQr
    data object RequiereEscanearMuseo : ResultadoValidacionQr
    data class MuseoIncorrecto(val museoEsperado: Museo) : ResultadoValidacionQr
}