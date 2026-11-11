package com.example.ars.features.museos.domain.usecase

import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ResultadoVisitaMuseo
import kotlinx.coroutines.CancellationException

class RegistrarVisitaMuseoUseCase(
    private val progresoRepository: ProgresoRepository,
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(museo: Museo): Result<ResultadoVisitaMuseo> {
        return try {
            val bienvenidaId = museo.criaturaBienvenidaId
            if (bienvenidaId != null) {
                check(homeRepository.traerCriaturas().any { it.id == bienvenidaId }) {
                    "La criatura de bienvenida no está en el catálogo."
                }
            }

            var yaVisitado = false
            var criaturaObtenidaId: String? = null
            progresoRepository.actualizarProgreso { progreso ->
                yaVisitado = museo.id in progreso.museosVisitados
                if (yaVisitado) {
                    progreso
                } else {
                    // si la criatura ya era tu starter, no la añadimos otra vez
                    criaturaObtenidaId = bienvenidaId?.takeIf {
                        it !in progreso.criaturasDescubiertas
                    }
                    progreso.copy(
                        museosVisitados = progreso.museosVisitados + museo.id,
                        criaturasDescubiertas = listOfNotNull(criaturaObtenidaId) +
                            progreso.criaturasDescubiertas
                    )
                }
            }
            Result.success(ResultadoVisitaMuseo(museo.id, yaVisitado, criaturaObtenidaId))
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
