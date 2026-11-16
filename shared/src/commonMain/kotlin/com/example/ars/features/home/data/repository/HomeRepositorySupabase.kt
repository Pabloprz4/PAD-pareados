package com.example.ars.features.home.data.repository

import com.example.ars.core.network.CatalogoRemoto
import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.repository.HomeRepository
import kotlin.coroutines.cancellation.CancellationException

// Criaturas leídas de Supabase.
// Si no hay conexión se usan las criaturas locales (HomeRepositoryImpl).
class HomeRepositorySupabase(
    private val catalogoRemoto: CatalogoRemoto,
    private val respaldoLocal: HomeRepository
) : HomeRepository {

    override suspend fun traerCriaturas(): List<Criatura> {
        return try {
            val museos = catalogoRemoto.obtenerMuseos().associateBy { it.id }

            catalogoRemoto.obtenerCriaturas()
                .map { criatura ->
                    Criatura(
                        id = criatura.id,
                        nombre = criatura.nombre,
                        museo = criatura.museoId
                            ?.let { museos[it]?.nombre }
                            ?: "Colección privada",
                        descripcion = criatura.descripcion
                    )
                }
                .ifEmpty { respaldoLocal.traerCriaturas() }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            respaldoLocal.traerCriaturas()
        }
    }
}
