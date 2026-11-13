package com.example.ars.features.museos.domain.model

data class ResultadoVisitaMuseo(
    val museoId: String,
    val yaVisitado: Boolean,
    val criaturaObtenidaId: String? = null,
    val recompensasIds: List<String> = emptyList()
)
