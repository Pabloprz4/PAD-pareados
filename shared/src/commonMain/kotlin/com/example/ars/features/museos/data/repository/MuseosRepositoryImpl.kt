
package com.example.ars.features.museos.data.repository

import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.Mision
import com.example.ars.features.museos.domain.model.ObjetivoMision
import com.example.ars.features.museos.domain.repository.MuseosRepository

class MuseosRepositoryImpl : MuseosRepository {

    override suspend fun obtenerMuseos(): List<Museo> {
        return listOf(
            // MUSEOS NACIONALES

            Museo(
                id = "museo_001",
                nombre = "Museo del Prado",
                ciudad = "Madrid",
                pais = "España",
                obrasDescubiertas = 21,
                obrasTotales = 50,
                criaturasDescubiertas = 3,
                criaturasTotales = 12,
                destacado = true,
                descripcion = "El Prado reúne obras de Velázquez, Goya y El Bosco. Descubre sus pinturas y las criaturas inspiradas en ellas.",
                criaturasIds = listOf("criatura_001", "criatura_004", "criatura_008"),
                criaturaBienvenidaId = "criatura_001",
                misiones = listOf(
                    Mision(
                        "prado_cantidad", "Escanear 2 obras distintas del Prado",
                        ObjetivoMision.CantidadObras(2), "criatura_008"
                    ),
                    Mision(
                        "prado_jardin", "Escanear El jardín de las delicias",
                        ObjetivoMision.ObrasConcretas(listOf("obra_002")), "criatura_004"
                    )
                )
            ),

            Museo(
                id = "museo_002",
                nombre = "Museo Reina Sofía",
                ciudad = "Madrid",
                pais = "España",
                obrasDescubiertas = 8,
                obrasTotales = 40,
                criaturasDescubiertas = 1,
                criaturasTotales = 10,
                destacado = true,
                descripcion = "El Reina Sofía presenta arte moderno y contemporáneo. En Ars puedes explorar las obras de Picasso y sus criaturas.",
                criaturasIds = listOf("criatura_002", "criatura_014"),
                misiones = listOf(
                    Mision(
                        "reina_cantidad", "Escanear 2 obras distintas del Reina Sofía",
                        ObjetivoMision.CantidadObras(2), "criatura_014"
                    ),
                    Mision(
                        "reina_guernica", "Escanear Guernica",
                        ObjetivoMision.ObrasConcretas(listOf("obra_003")), "criatura_002"
                    )
                )
            ),

            Museo(
                id = "museo_003",
                nombre = "Museo Thyssen-Bornemisza",
                ciudad = "Madrid",
                pais = "España",
                obrasDescubiertas = 0,
                obrasTotales = 35,
                criaturasDescubiertas = 0,
                criaturasTotales = 8,
                destacado = true,
                descripcion = "El Thyssen-Bornemisza reúne pintura de distintas épocas y estilos en Madrid.",
                criaturasIds = listOf("criatura_015"),
                recompensaMisionesId = "criatura_015",
                misiones = listOf(
                    Mision(
                        "thyssen_cantidad", "Escanear 2 obras distintas del Thyssen",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "thyssen_caballero", "Escanear Joven caballero en un paisaje",
                        ObjetivoMision.ObrasConcretas(listOf("obra_thyssen_caballero"))
                    )
                )
            ),

            // MUSEOS INTERNACIONALES

            Museo(
                id = "museo_004",
                nombre = "MoMA",
                ciudad = "Nueva York",
                pais = "Estados Unidos",
                obrasDescubiertas = 0,
                obrasTotales = 2,
                criaturasDescubiertas = 0,
                criaturasTotales = 2,
                descripcion = "El MoMA reúne arte moderno y contemporáneo en Nueva York.",
                criaturasIds = listOf("criatura_003", "criatura_009"),
                misiones = listOf(
                    Mision(
                        "moma_cantidad", "Escanear 2 obras distintas del MoMA",
                        ObjetivoMision.CantidadObras(2), "criatura_003"
                    ),
                    Mision(
                        "moma_noche", "Escanear La noche estrellada",
                        ObjetivoMision.ObrasConcretas(listOf("obra_moma_noche")), "criatura_009"
                    )
                )
            ),

            Museo(
                id = "museo_005",
                nombre = "Museo Van Gogh",
                ciudad = "Ámsterdam",
                pais = "Países Bajos",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "El Museo Van Gogh permite conocer la pintura y la trayectoria del artista en Ámsterdam.",
                criaturasIds = listOf("criatura_005"),
                recompensaMisionesId = "criatura_005",
                misiones = listOf(
                    Mision(
                        "vangogh_cantidad", "Escanear 2 obras distintas del Museo Van Gogh",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "vangogh_obra", "Escanear Los girasoles",
                        ObjetivoMision.ObrasConcretas(listOf("obra_vangogh_girasoles"))
                    )
                )
            ),

            Museo(
                id = "museo_006",
                nombre = "Galería Uffizi",
                ciudad = "Florencia",
                pais = "Italia",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "La Galería Uffizi reúne obras del Renacimiento en Florencia.",
                criaturasIds = listOf("criatura_006"),
                recompensaMisionesId = "criatura_006",
                misiones = listOf(
                    Mision(
                        "uffizi_cantidad", "Escanear 2 obras distintas de la Galería Uffizi",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "uffizi_obra", "Escanear El nacimiento de Venus",
                        ObjetivoMision.ObrasConcretas(listOf("obra_uffizi_venus"))
                    )
                )
            ),

            Museo(
                id = "museo_007",
                nombre = "Museo MUNCH",
                ciudad = "Oslo",
                pais = "Noruega",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "El Museo MUNCH está dedicado al artista Edvard Munch en Oslo.",
                criaturasIds = listOf("criatura_007"),
                recompensaMisionesId = "criatura_007",
                misiones = listOf(
                    Mision(
                        "munch_cantidad", "Escanear 2 obras distintas del Museo MUNCH",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "munch_obra", "Escanear El grito",
                        ObjetivoMision.ObrasConcretas(listOf("obra_munch_grito"))
                    )
                )
            ),

            Museo(
                id = "museo_008",
                nombre = "Museos Vaticanos",
                ciudad = "Ciudad del Vaticano",
                pais = "Ciudad del Vaticano",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "Los Museos Vaticanos reúnen colecciones artísticas en la Ciudad del Vaticano.",
                criaturasIds = listOf("criatura_010"),
                recompensaMisionesId = "criatura_010",
                misiones = listOf(
                    Mision(
                        "vaticano_cantidad", "Escanear 2 obras distintas de los Museos Vaticanos",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "vaticano_obra", "Escanear La creación de Adán",
                        ObjetivoMision.ObrasConcretas(listOf("obra_vaticano_creacion"))
                    )
                )
            ),

            Museo(
                id = "museo_009",
                nombre = "The Met",
                ciudad = "Nueva York",
                pais = "Estados Unidos",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "El Museo Metropolitano de Arte reúne obras de distintas culturas y épocas en Nueva York.",
                criaturasIds = listOf("criatura_011"),
                recompensaMisionesId = "criatura_011",
                misiones = listOf(
                    Mision(
                        "met_cantidad", "Escanear 2 obras distintas del Met",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "met_obra", "Escanear La gran ola de Kanagawa",
                        ObjetivoMision.ObrasConcretas(listOf("obra_met_ola"))
                    )
                )
            ),

            Museo(
                id = "museo_010",
                nombre = "Museo del Louvre",
                ciudad = "París",
                pais = "Francia",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1,
                descripcion = "El Louvre reúne colecciones artísticas en París.",
                criaturasIds = listOf("criatura_013"),
                recompensaMisionesId = "criatura_013",
                misiones = listOf(
                    Mision(
                        "louvre_cantidad", "Escanear 2 obras distintas del Louvre",
                        ObjetivoMision.CantidadObras(2)
                    ),
                    Mision(
                        "louvre_obra", "Escanear La Mona Lisa",
                        ObjetivoMision.ObrasConcretas(listOf("obra_louvre_mona_lisa"))
                    )
                )
            )
        )
    }
}
