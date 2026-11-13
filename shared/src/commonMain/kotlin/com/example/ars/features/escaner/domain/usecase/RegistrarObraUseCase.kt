package com.example.ars.features.escaner.domain.usecase

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.museos.domain.usecase.EvaluarMisionesUseCase
import com.example.ars.features.escaner.domain.model.ResultadoRegistroObra
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.museos.domain.model.Museo
import kotlinx.coroutines.CancellationException

class RegistrarObraUseCase(
    private val progresoRepository: ProgresoRepository,
    private val homeRepository: HomeRepository,
    private val catalogoRepository: CatalogoRepository,
    private val evaluarMisionesUseCase: EvaluarMisionesUseCase = EvaluarMisionesUseCase()
) {
    suspend operator fun invoke(obra: Obra, museo: Museo): Result<ResultadoRegistroObra> {
        return try {
            require(obra.museoId == museo.id) { "La obra no pertenece a este museo." }
            val bienvenidaId = museo.criaturaBienvenidaId
            if (bienvenidaId != null) {
                check(homeRepository.traerCriaturas().any { it.id == bienvenidaId }) {
                    "La criatura de bienvenida no está en el catálogo."
                }
            }

            val obras = catalogoRepository.obtenerObras()
            var recompensasIds: List<String> = emptyList()
            var yaEscaneada = false
            var criaturaBienvenidaId: String? = null
            progresoRepository.actualizarProgreso { progreso ->
                yaEscaneada = obra.id in progreso.obrasEscaneadas
                if (yaEscaneada) {
                    progreso
                } else {
                    // al leer una obra entendemos que ya estás dentro de su museo
                    val visita = progreso.conVisita(museo)
                    criaturaBienvenidaId = visita.criaturasDescubiertas.firstOrNull {
                        it !in progreso.criaturasDescubiertas
                    }
                    // obra, misiones y recompensas se guardan juntas
                    val conObra = visita.copy(obrasEscaneadas = progreso.obrasEscaneadas + obra.id)
                    val nuevo = evaluarMisionesUseCase(museo, obras, conObra).progreso
                    recompensasIds = nuevo.criaturasDescubiertas.filterNot {
                        it in conObra.criaturasDescubiertas
                    }
                    nuevo
                }
            }
            Result.success(ResultadoRegistroObra(obra.id, yaEscaneada, criaturaBienvenidaId, recompensasIds))
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
