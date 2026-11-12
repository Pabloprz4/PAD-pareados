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
            descripcion = "Velázquez representa a la infanta Margarita junto a sus acompañantes en el Alcázar de Madrid. El pintor aparece trabajando a la izquierda y un espejo refleja a los reyes al fondo."
        ),
        Obra(
            id = "obra_002",
            titulo = "El jardín de las delicias",
            autor = "El Bosco",
            museoId = "museo_001",
            descripcion = "Este tríptico del Bosco reúne el paraíso, un mundo de figuras y animales fantásticos y una visión del infierno. Sus escenas están llenas de pequeños detalles."
        ),
        Obra(
            id = "obra_003",
            titulo = "Guernica",
            autor = "Pablo Picasso",
            museoId = "museo_002",
            descripcion = "Picasso pintó esta obra en 1937. Sus figuras y sus tonos blancos, negros y grises expresan el sufrimiento y la violencia de la guerra."
        )
    )

    override suspend fun obtenerMuseoPorId(id: String): Museo? {
        // el QR busca en el mismo catálogo que muestran Inicio y Museos
        return museosRepository.obtenerMuseos()
            .firstOrNull { museo -> museo.id == id }
    }

    override suspend fun obtenerObras(): List<Obra> = obras

    override suspend fun obtenerObraPorId(id: String): Obra? {
        return obras.firstOrNull { obra -> obra.id == id }
    }
}