package com.example.ars.features.escaner.data.repository

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.repository.MuseosRepository

class CatalogoRepositoryImpl(
    private val museosRepository: MuseosRepository
) : CatalogoRepository {

    // catálogo local de obras para los QR
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
        ),
        Obra(
            id = "obra_reina_ventana",
            titulo = "Figura en una ventana",
            autor = "Salvador Dalí",
            museoId = "museo_002",
            descripcion = "Dalí retrata a su hermana Anna Maria de espaldas, mirando el mar desde una ventana en Cadaqués. La luz azul y la quietud de la figura crean una escena tranquila."
        ),
        Obra(
            id = "obra_thyssen_caballero",
            titulo = "Joven caballero en un paisaje",
            autor = "Vittore Carpaccio",
            museoId = "museo_003",
            descripcion = "Un joven con armadura sostiene su espada ante un paisaje lleno de plantas y animales. El armiño y las inscripciones aluden al honor y a las virtudes del caballero."
        ),
        Obra(
            id = "obra_thyssen_hotel",
            titulo = "Habitación de hotel",
            autor = "Edward Hopper",
            museoId = "museo_003",
            descripcion = "Una viajera sentada al borde de una cama consulta un horario de trenes, con el equipaje a su lado. Las líneas de la habitación y la luz intensa refuerzan su aislamiento."
        ),
        Obra(
            id = "obra_moma_noche",
            titulo = "La noche estrellada",
            autor = "Vincent van Gogh",
            museoId = "museo_004",
            descripcion = "Un cielo de estrellas y remolinos domina un pueblo al pie de las montañas. Van Gogh pintó esta vista imaginada en Saint-Rémy en 1889, con un gran ciprés en primer plano."
        ),
        Obra(
            id = "obra_moma_memoria",
            titulo = "La persistencia de la memoria",
            autor = "Salvador Dalí",
            museoId = "museo_004",
            descripcion = "Varios relojes blandos se doblan sobre una rama y otras superficies en un paisaje casi vacío. Dalí mezcla objetos reconocibles y formas extrañas para crear una escena de sueño."
        ),
        Obra(
            id = "obra_vangogh_girasoles",
            titulo = "Los girasoles",
            autor = "Vincent van Gogh",
            museoId = "museo_005",
            descripcion = "Un jarrón reúne girasoles en distintas fases de vida, pintados con muchos tonos de amarillo. Esta versión de 1889 pertenece al Museo Van Gogh de Ámsterdam."
        ),
        Obra(
            id = "obra_vangogh_almendro",
            titulo = "Almendro en flor",
            autor = "Vincent van Gogh",
            museoId = "museo_005",
            descripcion = "Las ramas de un almendro florecen sobre un cielo azul. Van Gogh pintó la obra en 1890 como regalo para su hermano Theo y su cuñada Jo por el nacimiento de su hijo."
        ),
        Obra(
            id = "obra_uffizi_venus",
            titulo = "El nacimiento de Venus",
            autor = "Sandro Botticelli",
            museoId = "museo_006",
            descripcion = "Venus llega a la costa sobre una concha, impulsada por el viento, mientras una figura se acerca con un manto. Botticelli convierte un relato de la mitología clásica en una imagen de belleza ideal."
        ),
        Obra(
            id = "obra_uffizi_primavera",
            titulo = "La primavera",
            autor = "Sandro Botticelli",
            museoId = "museo_006",
            descripcion = "Nueve personajes de la mitología se reúnen en un jardín de naranjos y flores. Venus ocupa el centro, con Cupido sobre ella y las tres Gracias danzando a su izquierda."
        ),
        Obra(
            id = "obra_munch_grito",
            titulo = "El grito",
            autor = "Edvard Munch",
            museoId = "museo_007",
            descripcion = "Una figura se lleva las manos a la cabeza bajo un cielo ondulado y rojizo. Sus líneas y colores transmiten angustia. Esta es la versión pintada hacia 1910 que conserva el MUNCH."
        ),
        Obra(
            id = "obra_munch_madonna",
            titulo = "Madonna",
            autor = "Edvard Munch",
            museoId = "museo_007",
            descripcion = "Una mujer de ojos cerrados aparece rodeada por líneas curvas y un halo rojo. Munch transforma el retrato con colores y formas que expresan emociones. Esta versión de 1894 pertenece al MUNCH."
        ),
        Obra(
            id = "obra_vaticano_creacion",
            titulo = "La creación de Adán",
            autor = "Miguel Ángel",
            museoId = "museo_008",
            descripcion = "Dios extiende su mano hacia Adán y sus dedos quedan a punto de tocarse. La escena forma parte de los frescos que Miguel Ángel pintó en la bóveda de la Capilla Sixtina."
        ),
        Obra(
            id = "obra_vaticano_atenas",
            titulo = "La escuela de Atenas",
            autor = "Rafael Sanzio",
            museoId = "museo_008",
            descripcion = "Platón y Aristóteles conversan en el centro de una arquitectura monumental, rodeados por pensadores de la Antigüedad. Este fresco decora la Estancia de la Signatura en el Vaticano."
        ),
        Obra(
            id = "obra_met_ola",
            titulo = "La gran ola de Kanagawa",
            autor = "Katsushika Hokusai",
            museoId = "museo_009",
            descripcion = "Una enorme ola se curva sobre varias barcas, mientras el monte Fuji aparece pequeño al fondo. Esta estampa pertenece a la serie Treinta y seis vistas del monte Fuji."
        ),
        Obra(
            id = "obra_met_washington",
            titulo = "Washington cruzando el Delaware",
            autor = "Emanuel Leutze",
            museoId = "museo_009",
            descripcion = "George Washington avanza en una barca entre bloques de hielo durante la guerra de Independencia de Estados Unidos. Leutze pintó en 1851 esta reconstrucción idealizada del cruce del río Delaware."
        ),
        Obra(
            id = "obra_louvre_mona_lisa",
            titulo = "La Mona Lisa",
            autor = "Leonardo da Vinci",
            museoId = "museo_010",
            descripcion = "Leonardo retrata a Lisa Gherardini sentada ante un paisaje lejano. Los contornos suaves del sfumato y su sonrisa sutil hacen de este retrato una de las obras más conocidas del Louvre."
        ),
        Obra(
            id = "obra_louvre_libertad",
            titulo = "La Libertad guiando al pueblo",
            autor = "Eugène Delacroix",
            museoId = "museo_010",
            descripcion = "Una figura que personifica la Libertad alza la bandera francesa y guía a un grupo sobre una barricada. Delacroix se inspiró en la revolución de julio de 1830 en París."
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