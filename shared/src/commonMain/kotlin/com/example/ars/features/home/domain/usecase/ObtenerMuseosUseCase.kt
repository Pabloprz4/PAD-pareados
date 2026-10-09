package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.Museo
import com.example.ars.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CancellationException

class ObtenerMuseosUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Museo>> {
        return try {
            val museos = repository.traerMuseos()
            if (museos.isEmpty()) {
                Result.failure(IllegalStateException("No hay museos disponibles"))
            } else {
                Result.success(museos)
            }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
