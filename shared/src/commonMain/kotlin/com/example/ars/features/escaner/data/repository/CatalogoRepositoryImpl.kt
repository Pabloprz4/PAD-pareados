package com.example.ars.features.escaner.data.repository

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.domain.repository.HomeRepository

class CatalogoRepositoryImpl(
    private val homeRepository: HomeRepository
) : CatalogoRepository {

    // catalogo local para prueba
    private val obras = listOf(
        Obra(
            id = "obra_001",
            titulo = "Las meninas",
            autor = "Diego Velázquez",
            museoId = "museo_001",
            descripcion = "Descripción de muestra para la ficha."
        ),
        Obra(
            id = "obra_002",
            titulo = "El jardín de las delicias",
            autor = "El Bosco",
            museoId = "museo_001",
            descripcion = "Descripción de muestra para la ficha."
        ),
        Obra(
            id = "obra_003",
            titulo = "Guernica",
            autor = "Pablo Picasso",
            museoId = "museo_002",
            descripcion = "Descripción de muestra para la ficha."
        )
    )

    override suspend fun obtenerMuseoPorId(id: String): Museo? {
        return homeRepository.traerMuseos()
            .firstOrNull { museo -> museo.id == id }
    }

    override suspend fun obtenerObraPorId(id: String): Obra? {
        return obras.firstOrNull { obra -> obra.id == id }
    }
}