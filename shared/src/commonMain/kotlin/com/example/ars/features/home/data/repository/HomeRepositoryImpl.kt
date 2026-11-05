package com.example.ars.features.home.data.repository

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.model.Museo
class HomeRepositoryImpl : HomeRepository {

    override suspend fun traerCriaturas(): List<Criatura> {
        // La cosa sería llamar a una base de datos local en vez de esto, pero de momento ponemos esto perracos
        return listOf(
            Criatura(
                nombre = "Menina Fantasmal",
                museo = "Museo del Prado",
                descripcion = "Criatura conseguida automáticamente al visitar el Museo del Prado."
            ),
            Criatura(
                nombre = "Caballo Guernica",
                museo = "Museo Reina Sofía",
                descripcion = "Criatura cubista desbloqueada al fotografiar cuadros del Reina Sofía."
            ),
            Criatura(
                nombre = "Reloj Derretido",
                museo = "MoMA (Nueva York)",
                descripcion = "Criatura surrealista inspirada en La persistencia de la memoria de Dalí."
            ),
            Criatura(
                nombre = "Humanoide del Jardín",
                museo = "Museo del Prado",
                descripcion = "Humanoide inspirado en El jardín de las delicias de El Bosco."
            ),
            Criatura(
                nombre = "Girasol Ardiente",
                museo = "Museo Van Gogh",
                descripcion = "Girasol ardiente inspirado en Los girasoles de Vincent van Gogh."
            ),
            Criatura(
                nombre = "Ninfa de Venus",
                museo = "Galería Uffizi (Florencia)",
                descripcion = "Pequeña ninfa inspirada en El nacimiento de Venus de Sandro Botticelli."
            ),
            Criatura(
                nombre = "Espectro del Grito",
                museo = "Museo MUNCH (Oslo)",
                descripcion = "Espectro inspirado en El grito de Edvard Munch."
            ),
            Criatura(
                nombre = "Saturno Devorador",
                museo = "Museo del Prado",
                descripcion = "Criatura inspirada en Saturno devorando a su hijo de Francisco de Goya."
            ),
            Criatura(
                nombre = "Remolino Estrellado",
                museo = "MoMA (Nueva York)",
                descripcion = "Remolino flotante inspirado en La noche estrellada de Vincent van Gogh."
            ),
            Criatura(
                nombre = "Manos de la Creación",
                museo = "Museos Vaticanos",
                descripcion = "Manos flotantes inspiradas en La creación de Adán de Miguel Ángel."
            ),
            Criatura(
                nombre = "Garra de Kanagawa",
                museo = "Museo Metropolitano de Arte (Nueva York)",
                descripcion = "Garra de agua inspirada en La gran ola de Kanagawa de Katsushika Hokusai."
            ),
            Criatura(
                nombre = "Manzana con Traje",
                museo = "Colección privada",
                descripcion = "Manzana con traje inspirada en El hijo del hombre de René Magritte."
            ),
            Criatura(
                nombre = "Dama de la Mona Lisa",
                museo = "Museo del Louvre",
                descripcion = "Dama flotante inspirada en La Mona Lisa de Leonardo da Vinci."
            ),
            Criatura(
                nombre = "Toro Guernica",
                museo = "Museo Reina Sofía",
                descripcion = "Criatura cubista inspirada en el toro del Guernica de Pablo Picasso."
            )
        )
    }



    override suspend fun traerMuseos(): List<Museo> {
        return listOf(
            // NACIONALES
            Museo(
                nombre = "Museo del Prado",
                ciudad = "Madrid",
                obrasDescubiertas = 21,
                obrasTotales = 50,
                criaturasDescubiertas = 3,
                criaturasTotales = 12,
                id = "museo_001",
                pais = "España",
                destacado = true
            ),
            Museo(
                nombre = "Museo Reina Sofía",
                ciudad = "Madrid",
                obrasDescubiertas = 8,
                obrasTotales = 40,
                criaturasDescubiertas = 1,
                criaturasTotales = 10,
                id = "museo_002",
                pais = "España",
                destacado = true
            ),
            Museo(
                nombre = "Museo Thyssen-Bornemisza",
                ciudad = "Madrid",
                obrasDescubiertas = 0,
                obrasTotales = 35,
                criaturasDescubiertas = 0,
                criaturasTotales = 8,
                id = "museo_003",
                pais = "España",
                destacado = true
            ),

            // INTERNACIONALES
            Museo(
                nombre = "MoMA (Nueva York)",
                ciudad = "Nueva York",
                obrasDescubiertas = 0,
                obrasTotales = 30,
                criaturasDescubiertas = 0,
                criaturasTotales = 2,
                id = "museo_004",
                pais = "Estados Unidos"
            ),
            Museo(
                nombre = "Museo Van Gogh",
                ciudad = "Ámsterdam",
                obrasDescubiertas = 0,
                obrasTotales = 25,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_005",
                pais = "Países Bajos"
            ),
            Museo(
                nombre = "Galería Uffizi (Florencia)",
                ciudad = "Florencia",
                obrasDescubiertas = 0,
                obrasTotales = 30,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_006",
                pais = "Italia"
            ),
            Museo(
                nombre = "Museo MUNCH (Oslo)",
                ciudad = "Oslo",
                obrasDescubiertas = 0,
                obrasTotales = 25,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_007",
                pais = "Noruega"
            ),
            Museo(
                nombre = "Museos Vaticanos",
                ciudad = "Ciudad del Vaticano",
                obrasDescubiertas = 0,
                obrasTotales = 30,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_008",
                pais = "Ciudad del Vaticano"
            ),
            Museo(
                nombre = "Museo Metropolitano de Arte (Nueva York)",
                ciudad = "Nueva York",
                obrasDescubiertas = 0,
                obrasTotales = 35,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_009",
                pais = "Estados Unidos"
            ),
            Museo(
                nombre = "Museo del Louvre",
                ciudad = "París",
                obrasDescubiertas = 0,
                obrasTotales = 40,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                id = "museo_010",
                pais = "Francia"
            )
        )
    }


}