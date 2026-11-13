package com.example.ars.features.museos.domain.model

sealed interface ObjetivoMision {
    data class CantidadObras(val cantidad: Int) : ObjetivoMision {
        init { require(cantidad > 0) }
    }

    data class ObrasConcretas(val ids: List<String>) : ObjetivoMision {
        init { require(ids.size in 1..4 && ids.distinct().size == ids.size) }
    }
}

data class Mision(
    val id: String,
    val titulo: String,
    val objetivo: ObjetivoMision,
    val recompensaCriaturaId: String? = null
)
