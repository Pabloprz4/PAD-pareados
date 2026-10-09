package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CancellationException

class ObtenerCriaturasUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Criatura>> {
        return try {
            val criaturas = repository.traerCriaturas()
            if (criaturas.isEmpty()) {
                Result.failure(IllegalStateException("No hay criaturas disponibles"))
            } else {
                Result.success(criaturas)
            }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
