
package com.example.ars.features.museos.presentation

import com.example.ars.features.museos.domain.model.Museo

data class MuseosUiState(
    val museos: List<Museo> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
) {
    val museosNacionales: List<Museo>
        get() = museos.filter { it.pais == "España" }

    val museosInternacionales: List<Museo>
        get() = museos.filter { it.pais != "España" }

    val obrasDescubiertas: Int
        get() = museos.sumOf { it.obrasDescubiertas }

    val obrasTotales: Int
        get() = museos.sumOf { it.obrasTotales }

    val progresoGlobal: Float
        get() = if (obrasTotales > 0) {
            obrasDescubiertas.toFloat() / obrasTotales
        } else {
            0f
        }
}
