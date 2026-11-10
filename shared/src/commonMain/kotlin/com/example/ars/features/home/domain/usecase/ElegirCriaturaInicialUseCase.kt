package com.example.ars.features.home.domain.usecase

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ElegirCriaturaInicialUseCase(
    private val repository: ProgresoRepository
) {

    private val mutex = Mutex()//hace que dos llamadas se ejecuten 1 despues de otra

    suspend operator fun invoke(
        criaturaId: String
    ): Result<ProgresoUsuario> {
        return try {
            mutex.withLock {
                require(criaturaId in IDS_INICIALES) {
                    "Esta criatura no está disponible como criatura inicial."
                }

                val progreso = repository.obtenerProgreso()

                check(progreso.criaturaInicialId == null) {
                    "Ya has elegido tu criatura inicial."
                }

                val nuevoProgreso = progreso.copy(
                    criaturaInicialId = criaturaId,
                    criaturasDescubiertas = listOf(criaturaId) +
                            progreso.criaturasDescubiertas.filterNot {
                                it == criaturaId
                            }
                )

                repository.guardarProgreso(nuevoProgreso)

                Result.success(nuevoProgreso)
            }
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