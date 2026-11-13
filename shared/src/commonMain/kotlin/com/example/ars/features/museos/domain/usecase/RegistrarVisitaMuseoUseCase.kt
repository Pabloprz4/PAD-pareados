package com.example.ars.features.museos.domain.usecase

import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ResultadoVisitaMuseo
import kotlinx.coroutines.CancellationException

class RegistrarVisitaMuseoUseCase(
    private val progresoRepository: ProgresoRepository,
    private val homeRepository: HomeRepository,
    private val catalogoRepository: CatalogoRepository,
    private val evaluarMisionesUseCase: EvaluarMisionesUseCase = EvaluarMisionesUseCase()
) {
    suspend operator fun invoke(museo: Museo): Result<ResultadoVisitaMuseo> {
        return try {
            val bienvenidaId = museo.criaturaBienvenidaId
            if (bienvenidaId != null) {
                check(homeRepository.traerCriaturas().any { it.id == bienvenidaId }) {
                    "La criatura de bienvenida no está en el catálogo."
                }
            }

            val obras = catalogoRepository.obtenerObras()
            var recompensasIds: List<String> = emptyList()
            var yaVisitado = false
            var criaturaObtenidaId: String? = null
            progresoRepository.actualizarProgreso { progreso ->
                yaVisitado = museo.id in progreso.museosVisitados
                val nuevo = progreso.conVisita(museo)
                // si la criatura ya era tu starter, no la añadimos otra vez
                criaturaObtenidaId = nuevo.criaturasDescubiertas.firstOrNull {
                    it !in progreso.criaturasDescubiertas
                }
                val evaluado = evaluarMisionesUseCase(museo, obras, nuevo).progreso
                recompensasIds = evaluado.criaturasDescubiertas.filterNot {
                    it in nuevo.criaturasDescubiertas
                }
                evaluado
            }
            Result.success(ResultadoVisitaMuseo(museo.id, yaVisitado, criaturaObtenidaId, recompensasIds))
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
