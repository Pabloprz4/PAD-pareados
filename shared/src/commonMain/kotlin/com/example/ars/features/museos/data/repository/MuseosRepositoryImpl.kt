
package com.example.ars.features.museos.data.repository

import com.example.ars.features.museos.domain.model.Museo
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
                destacado = true
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
                destacado = true
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
                destacado = true
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
                criaturasTotales = 2
            ),

            Museo(
                id = "museo_005",
                nombre = "Museo Van Gogh",
                ciudad = "Ámsterdam",
                pais = "Países Bajos",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            ),

            Museo(
                id = "museo_006",
                nombre = "Galería Uffizi",
                ciudad = "Florencia",
                pais = "Italia",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            ),

            Museo(
                id = "museo_007",
                nombre = "Museo MUNCH",
                ciudad = "Oslo",
                pais = "Noruega",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            ),

            Museo(
                id = "museo_008",
                nombre = "Museos Vaticanos",
                ciudad = "Ciudad del Vaticano",
                pais = "Ciudad del Vaticano",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            ),

            Museo(
                id = "museo_009",
                nombre = "The Met",
                ciudad = "Nueva York",
                pais = "Estados Unidos",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            ),

            Museo(
                id = "museo_010",
                nombre = "Museo del Louvre",
                ciudad = "París",
                pais = "Francia",
                obrasDescubiertas = 0,
                obrasTotales = 1,
                criaturasDescubiertas = 0,
                criaturasTotales = 1
            )
        )
    }
}
