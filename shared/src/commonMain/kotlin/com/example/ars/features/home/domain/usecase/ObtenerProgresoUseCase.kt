package com.example.ars.features.home.domain.usecase

import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.museos.domain.repository.MuseosRepository
import com.example.ars.features.museos.domain.usecase.EvaluarMisionesUseCase
import kotlinx.coroutines.CancellationException

class ObtenerProgresoUseCase(
    private val repository: ProgresoRepository,
    private val museosRepository: MuseosRepository,
    private val catalogoRepository: CatalogoRepository,
    private val evaluarMisionesUseCase: EvaluarMisionesUseCase = EvaluarMisionesUseCase()
) {
    suspend operator fun invoke(): Result<ProgresoUsuario> {
        return try {
            val museos = museosRepository.obtenerMuseos()
            val obras = catalogoRepository.obtenerObras()
            val progreso = repository.actualizarProgreso { anterior ->
                // también cuentan las obras guardadas antes de añadir las misiones
                var actualizado = anterior
                museos.forEach { museo ->
                    actualizado = evaluarMisionesUseCase(museo, obras, actualizado).progreso
                }
                actualizado
            }
            Result.success(progreso)
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
