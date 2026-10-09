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
            Museo(
                nombre = "Museo del Prado",
                ciudad = "Madrid",
                obrasDescubiertas = 21,
                obrasTotales = 50,
                criaturasDescubiertas = 3,
                criaturasTotales = 12
            ),
            Museo(
                nombre = "Museo Reina Sofía",
                ciudad = "Madrid",
                obrasDescubiertas = 8,
                obrasTotales = 40,
                criaturasDescubiertas = 1,
                criaturasTotales = 10
            ),
            Museo(
                nombre = "Museo Thyssen-Bornemisza",
                ciudad = "Madrid",
                obrasDescubiertas = 0,
                obrasTotales = 35,
                criaturasDescubiertas = 0,
                criaturasTotales = 8
            )
        )
    }
}