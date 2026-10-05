package com.example.ars.features.home.data.repository

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository

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
            )
        )
    }
}