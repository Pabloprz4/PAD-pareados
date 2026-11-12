package com.example.ars.features.escaner.domain.model

import com.example.ars.features.museos.domain.model.Museo

sealed interface ResultadoValidacionQr {
    data class MuseoValido(val museo: Museo) : ResultadoValidacionQr
    data class ObraValida(val obra: Obra, val museo: Museo) : ResultadoValidacionQr
    data object CodigoInvalido : ResultadoValidacionQr
    data object MuseoDesconocido : ResultadoValidacionQr
    data object ObraDesconocida : ResultadoValidacionQr
}