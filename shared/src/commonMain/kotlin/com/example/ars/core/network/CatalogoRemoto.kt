package com.example.ars.core.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Lee el catálogo (museos, criaturas, obras y misiones) de Supabase.
// El catálogo casi no cambia, así que se descarga una vez y se guarda en memoria
// para no repetir peticiones cada vez que una pantalla lo necesita.
class CatalogoRemoto(
    private val cliente: SupabaseClient
) {
    private val mutex = Mutex()

    private var museos: List<MuseoDto>? = null
    private var criaturas: List<CriaturaDto>? = null
    private var obras: List<ObraDto>? = null
    private var misiones: List<MisionDto>? = null

    suspend fun obtenerMuseos(): List<MuseoDto> = mutex.withLock {
        museos ?: cliente.from("museo")
            .select { order("id", Order.ASCENDING) }
            .decodeList<MuseoDto>()
            .also { museos = it }
    }

    suspend fun obtenerCriaturas(): List<CriaturaDto> = mutex.withLock {
        criaturas ?: cliente.from("criatura")
            .select { order("id", Order.ASCENDING) }
            .decodeList<CriaturaDto>()
            .also { criaturas = it }
    }

    suspend fun obtenerObras(): List<ObraDto> = mutex.withLock {
        obras ?: cliente.from("obra")
            .select()
            .decodeList<ObraDto>()
            .also { obras = it }
    }

    suspend fun obtenerMisiones(): List<MisionDto> = mutex.withLock {
        // "mision_obra(obra_id)" trae también las obras de cada misión (JOIN)
        misiones ?: cliente.from("mision")
            .select(Columns.raw("*, mision_obra(obra_id)")) { order("orden", Order.ASCENDING) }
            .decodeList<MisionDto>()
            .also { misiones = it }
    }
}
