package com.example.ars.features.escaner.data.repository

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.repository.MuseosRepository

class CatalogoRepositoryImpl(
    private val museosRepository: MuseosRepository
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
        // el QR busca en el mismo catálogo que muestran Inicio y Museos
        return museosRepository.obtenerMuseos()
            .firstOrNull { museo -> museo.id == id }
    }

    override suspend fun obtenerObraPorId(id: String): Obra? {
        return obras.firstOrNull { obra -> obra.id == id }
    }
}