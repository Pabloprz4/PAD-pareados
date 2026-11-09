package com.example.ars.features.escaner.domain.repository

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.museos.domain.model.Museo

interface CatalogoRepository {

    // si id no existe, null
    suspend fun obtenerMuseoPorId(id: String): Museo?
    suspend fun obtenerObraPorId(id: String): Obra?
}