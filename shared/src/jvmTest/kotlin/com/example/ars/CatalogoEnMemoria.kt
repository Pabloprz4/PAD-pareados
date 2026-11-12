package com.example.ars

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.museos.domain.model.Museo

class CatalogoEnMemoria(
    private val obras: List<Obra> = emptyList(),
    private val museos: List<Museo> = emptyList()
) : CatalogoRepository {
    override suspend fun obtenerObras() = obras
    override suspend fun obtenerObraPorId(id: String) = obras.firstOrNull { it.id == id }
    override suspend fun obtenerMuseoPorId(id: String) = museos.firstOrNull { it.id == id }
}
