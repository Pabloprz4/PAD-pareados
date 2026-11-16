package com.example.ars.features.escaner.data.repository

import com.example.ars.core.network.CatalogoRemoto
import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.repository.MuseosRepository
import kotlin.coroutines.cancellation.CancellationException

// Obras leídas de Supabase (las que se registran al escanear su QR).
// Si no hay conexión se usan las obras locales (CatalogoRepositoryImpl).
class CatalogoRepositorySupabase(
    private val catalogoRemoto: CatalogoRemoto,
    private val museosRepository: MuseosRepository,
    private val respaldoLocal: CatalogoRepository
) : CatalogoRepository {

    override suspend fun obtenerMuseoPorId(id: String): Museo? {
        // el QR busca en el mismo catálogo que muestran Inicio y Museos
        return museosRepository.obtenerMuseos()
            .firstOrNull { museo -> museo.id == id }
    }

    override suspend fun obtenerObras(): List<Obra> {
        return try {
            catalogoRemoto.obtenerObras()
                .map { obra ->
                    Obra(
                        id = obra.id,
                        titulo = obra.titulo,
                        autor = obra.autor,
                        museoId = obra.museoId,
                        descripcion = obra.descripcion
                    )
                }
                .ifEmpty { respaldoLocal.obtenerObras() }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            respaldoLocal.obtenerObras()
        }
    }

    override suspend fun obtenerObraPorId(id: String): Obra? {
        return obtenerObras().firstOrNull { obra -> obra.id == id }
    }
}
