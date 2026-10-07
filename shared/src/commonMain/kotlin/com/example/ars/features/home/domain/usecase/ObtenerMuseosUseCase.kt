package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.domain.repository.HomeRepository

class ObtenerMuseosUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Museo>> {
        val museos = repository.traerMuseos()

        if (museos.isEmpty()) {
            return Result.failure(
                IllegalStateException("No hay museos disponibles")
            )
        }

        return Result.success(museos)
    }
}