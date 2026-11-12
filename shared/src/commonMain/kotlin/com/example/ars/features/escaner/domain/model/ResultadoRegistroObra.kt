package com.example.ars.features.escaner.domain.model

data class ResultadoRegistroObra(
    val obraId: String,
    val yaEscaneada: Boolean,
    val criaturaBienvenidaId: String? = null
)
