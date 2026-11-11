package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import kotlinx.coroutines.CancellationException

class ElegirCriaturaInicialUseCase(
    private val repository: ProgresoRepository
) {

    suspend operator fun invoke(
        criaturaId: String
    ): Result<ProgresoUsuario> {
        return try {
            val nuevoProgreso = repository.actualizarProgreso { progreso ->
                require(criaturaId in IDS_INICIALES) {
                    "Esta criatura no está disponible como criatura inicial."
                }

                check(progreso.criaturaInicialId == null) {
                    "Ya has elegido tu criatura inicial."
                }

                progreso.copy(
                    criaturaInicialId = criaturaId,
                    criaturasDescubiertas = listOf(criaturaId) +
                            progreso.criaturasDescubiertas.filterNot {
                                it == criaturaId
                            }
                )
            }
            Result.success(nuevoProgreso)
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    companion object {
        val IDS_INICIALES: List<String> = listOf(
            "criatura_001", // Menina Fantasmal
            "criatura_012", // Manzana con Traje
            "criatura_002"  // Caballo Guernica
        )
    }
}