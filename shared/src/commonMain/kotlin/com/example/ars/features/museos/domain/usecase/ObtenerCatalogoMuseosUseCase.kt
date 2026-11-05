
package com.example.ars.features.museos.domain.usecase

import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.repository.MuseosRepository
import kotlinx.coroutines.CancellationException

class ObtenerCatalogoMuseosUseCase(
    private val repository: MuseosRepository
) {
    suspend operator fun invoke(): Result<List<Museo>> {
        return try {
            val museos = repository.obtenerMuseos()

            if (museos.isEmpty()) {
                Result.failure(
                    IllegalStateException("No hay museos disponibles")
                )
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
