package com.example.ars.features.home.data.repository

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProgresoRepositorySupabase(
    private val cliente: SupabaseClient,
    private val usuarioId: String
) : ProgresoRepository {
    private val mutex = Mutex()

    override suspend fun obtenerProgreso(): ProgresoUsuario = consultar()

    override suspend fun guardarProgreso(progreso: ProgresoUsuario) {
        actualizarProgreso { progreso }
    }

    override suspend fun actualizarProgreso(
        cambio: (ProgresoUsuario) -> ProgresoUsuario
    ): ProgresoUsuario = mutex.withLock {
        val anterior = consultar()
        val nuevo = cambio(anterior)
        if (nuevo == anterior) return@withLock anterior

        // Solo se sube el avance nuevo para conservar lo guardado desde otro dispositivo.
        val cambios = ProgresoUsuario(
            criaturaInicialId = nuevo.criaturaInicialId?.takeIf { it != anterior.criaturaInicialId },
            criaturasDescubiertas = nuevo.criaturasDescubiertas - anterior.criaturasDescubiertas.toSet(),
            museosVisitados = nuevo.museosVisitados - anterior.museosVisitados.toSet(),
            obrasEscaneadas = nuevo.obrasEscaneadas - anterior.obrasEscaneadas.toSet(),
            misionesCompletadas = nuevo.misionesCompletadas - anterior.misionesCompletadas.toSet()
        )

        consultar(cambios)
    }

    private suspend fun consultar(cambios: ProgresoUsuario? = null): ProgresoUsuario {
        check(cliente.auth.currentUserOrNull()?.id == usuarioId) { "La sesión ha cambiado." }

        val parametros = buildJsonObject {
            // La base de datos también comprueba la cuenta si la sesión cambia durante la petición.
            put("usuario_esperado", usuarioId)
            cambios?.let { put("cambios", Json.encodeToJsonElement(ProgresoUsuario.serializer(), it)) }
        }
        return cliente.postgrest.rpc("sincronizar_progreso_ars", parametros).decodeAs<ProgresoUsuario>()
    }
}
