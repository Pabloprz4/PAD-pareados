
package com.example.ars.features.museos.domain.usecase

import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.repository.MuseosRepository
import kotlinx.coroutines.CancellationException
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.home.domain.repository.ProgresoRepository

class ObtenerCatalogoMuseosUseCase(
    private val repository: MuseosRepository,
    private val catalogoRepository: CatalogoRepository,
    private val progresoRepository: ProgresoRepository
) {
    suspend operator fun invoke(): Result<List<Museo>> {
        return try {
            val museos = repository.obtenerMuseos()

            if (museos.isEmpty()) {
                Result.failure(
                    IllegalStateException("No hay museos disponibles")
                )
            } else {
                val obras = catalogoRepository.obtenerObras()
                val progreso = progresoRepository.obtenerProgreso()
                Result.success(museos.map { museo ->
                    val obrasMuseo = obras.filter { it.museoId == museo.id }
                    museo.copy(
                        obrasTotales = obrasMuseo.size,
                        obrasDescubiertas = obrasMuseo.count { it.id in progreso.obrasEscaneadas },
                        criaturasTotales = museo.criaturasIds.size,
                        criaturasDescubiertas = museo.criaturasIds.count {
                            it in progreso.criaturasDescubiertas
                        }
                    )
                })
            }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
