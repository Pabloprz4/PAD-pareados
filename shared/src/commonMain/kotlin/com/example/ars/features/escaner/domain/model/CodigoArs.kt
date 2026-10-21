package com.example.ars.features.escaner.domain.model

sealed interface CodigoArs{

    // ids
    data class Museo(
        val museoId: String
    ) : CodigoArs

    data class Obra(
        val obraId: String
    ) : CodigoArs
}