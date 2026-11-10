package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import kotlinx.coroutines.CancellationException

class ObtenerProgresoUseCase(
    private val repository: ProgresoRepository
) {

    suspend operator fun invoke(): Result<ProgresoUsuario> {
        return try {
            Result.success(repository.obtenerProgreso())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}