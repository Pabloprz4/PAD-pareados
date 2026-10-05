package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository

class ObtenerCriaturasUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Criatura>> {
        val criaturas = repository.traerCriaturas()
        if (criaturas.isEmpty()) {
            return Result.failure(IllegalStateException("No hay criaturas disponibles"))
        }
        return Result.success(criaturas)
    }
}