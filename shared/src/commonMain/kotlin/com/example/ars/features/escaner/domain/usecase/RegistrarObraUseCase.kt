package com.example.ars.features.escaner.domain.usecase

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.model.ResultadoRegistroObra
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.museos.domain.model.Museo
import kotlinx.coroutines.CancellationException

class RegistrarObraUseCase(
    private val progresoRepository: ProgresoRepository,
    private val homeRepository: HomeRepository
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
                    // visita y obra se guardan juntas, una sola vez
                    visita.copy(obrasEscaneadas = progreso.obrasEscaneadas + obra.id)
                }
            }
            Result.success(ResultadoRegistroObra(obra.id, yaEscaneada, criaturaBienvenidaId))
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
