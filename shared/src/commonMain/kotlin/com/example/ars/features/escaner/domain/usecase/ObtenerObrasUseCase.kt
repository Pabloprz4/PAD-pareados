package com.example.ars.features.escaner.domain.usecase

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import kotlinx.coroutines.CancellationException

class ObtenerObrasUseCase(private val repository: CatalogoRepository) {
    suspend operator fun invoke(): Result<List<Obra>> {
        return try {
            Result.success(repository.obtenerObras())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
