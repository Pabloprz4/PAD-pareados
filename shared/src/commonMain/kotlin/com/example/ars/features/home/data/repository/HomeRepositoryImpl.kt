package com.example.ars.features.home.data.repository

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl : HomeRepository {

    override suspend fun traerCriaturas(): List<Criatura> {
        return listOf(
            Criatura(
                id = "criatura_001",
                nombre = "Menina Fantasmal",
                museo = "Museo del Prado",
                descripcion = "Criatura conseguida automáticamente al visitar el Museo del Prado."
            ),
            Criatura(
                id = "criatura_002",
                nombre = "Caballo Guernica",
                museo = "Museo Reina Sofía",
                descripcion = "Criatura cubista desbloqueada al fotografiar cuadros del Reina Sofía."
            ),
            Criatura(
                id = "criatura_003",
                nombre = "Reloj Derretido",
                museo = "MoMA (Nueva York)",
                descripcion = "Criatura surrealista inspirada en La persistencia de la memoria de Dalí."
            ),
            Criatura(
                id = "criatura_004",
                nombre = "Humanoide del Jardín",
                museo = "Museo del Prado",
                descripcion = "Humanoide inspirado en El jardín de las delicias de El Bosco."
            ),
            Criatura(
                id = "criatura_005",
                nombre = "Girasol Ardiente",
                museo = "Museo Van Gogh",
                descripcion = "Girasol ardiente inspirado en Los girasoles de Vincent van Gogh."
            ),
            Criatura(
                id = "criatura_006",
                nombre = "Ninfa de Venus",
                museo = "Galería Uffizi (Florencia)",
                descripcion = "Pequeña ninfa inspirada en El nacimiento de Venus de Sandro Botticelli."
            ),
            Criatura(
                id = "criatura_007",
                nombre = "Espectro del Grito",
                museo = "Museo MUNCH (Oslo)",
                descripcion = "Espectro inspirado en El grito de Edvard Munch."
            ),
            Criatura(
                id = "criatura_008",
                nombre = "Saturno Devorador",
                museo = "Museo del Prado",
                descripcion = "Criatura inspirada en Saturno devorando a su hijo de Francisco de Goya."
            ),
            Criatura(
                id = "criatura_009",
                nombre = "Remolino Estrellado",
                museo = "MoMA (Nueva York)",
                descripcion = "Remolino flotante inspirado en La noche estrellada de Vincent van Gogh."
            ),
            Criatura(
                id = "criatura_010",
                nombre = "Manos de la Creación",
                museo = "Museos Vaticanos",
                descripcion = "Manos flotantes inspiradas en La creación de Adán de Miguel Ángel."
            ),
            Criatura(
                id = "criatura_011",
                nombre = "Garra de Kanagawa",
                museo = "Museo Metropolitano de Arte (Nueva York)",
                descripcion = "Garra de agua inspirada en La gran ola de Kanagawa de Katsushika Hokusai."
            ),
            Criatura(
                id = "criatura_012",
                nombre = "Manzana con Traje",
                museo = "Colección privada",
                descripcion = "Manzana con traje inspirada en El hijo del hombre de René Magritte."
            ),
            Criatura(
                id = "criatura_013",
                nombre = "Dama de la Mona Lisa",
                museo = "Museo del Louvre",
                descripcion = "Dama flotante inspirada en La Mona Lisa de Leonardo da Vinci."
            ),
            Criatura(
                id = "criatura_014",
                nombre = "Toro Guernica",
                museo = "Museo Reina Sofía",
                descripcion = "Criatura cubista inspirada en el toro del Guernica de Pablo Picasso."
            )
        )
    }
}