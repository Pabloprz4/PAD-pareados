package com.example.ars.features.home.data.repository

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

// Progreso del usuario guardado en Supabase.
//
// · El progreso se sigue guardando también en local (ProgresoRepositoryImpl), así la app
//   funciona sin conexión y las pantallas no esperan a la red.
// · Al abrir la app se sincroniza una vez: se une lo que hay en Supabase con lo local.
// · Cada cambio se guarda en local y después se sube a Supabase.
// · Cada instalación tiene un usuario anónimo de Supabase Auth, sin email ni contraseña.
class ProgresoRepositorySupabase(
    private val cliente: SupabaseClient,
    private val local: ProgresoRepository
) : ProgresoRepository {

    private val mutex = Mutex()
    private var sincronizado = false
    private var ultimoIntento: TimeMark? = null
    private var ultimoSubido: ProgresoUsuario? = null

    override suspend fun obtenerProgreso(): ProgresoUsuario {
        sincronizarSiHaceFalta()
        return local.obtenerProgreso()
    }

    override suspend fun guardarProgreso(progreso: ProgresoUsuario) {
        local.guardarProgreso(progreso)
        subirSinFallar(progreso)
    }

    override suspend fun actualizarProgreso(
        cambio: (ProgresoUsuario) -> ProgresoUsuario
    ): ProgresoUsuario {
        sincronizarSiHaceFalta()
        val nuevo = local.actualizarProgreso(cambio)
        subirSinFallar(nuevo)
        return nuevo
    }

    // Descarga el progreso de Supabase y lo une con el local (solo una vez por sesión).
    // Si no hay conexión no se vuelve a intentar hasta pasados 30 segundos.
    private suspend fun sincronizarSiHaceFalta() = mutex.withLock {
        if (sincronizado) return@withLock
        val intento = ultimoIntento
        if (intento != null && intento.elapsedNow() < 30.seconds) return@withLock
        ultimoIntento = TimeSource.Monotonic.markNow()

        try {
            val usuarioId = asegurarSesion()
            val remoto = leerRemoto()
            val combinado = local.actualizarProgreso { combinar(it, remoto) }
            if (combinado != remoto) subir(usuarioId, combinado)
            ultimoSubido = combinado
            sincronizado = true
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            // sin conexión: se sigue usando el progreso local
        }
    }

    private suspend fun subirSinFallar(progreso: ProgresoUsuario) = mutex.withLock {
        if (progreso == ultimoSubido) return@withLock
        try {
            subir(asegurarSesion(), progreso)
            ultimoSubido = progreso
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            // se subirá en la próxima sincronización
        }
    }

    // Inicia sesión como usuario anónimo la primera vez; después la sesión se guarda sola.
    private suspend fun asegurarSesion(): String {
        val auth = cliente.auth
        auth.awaitInitialization()
        if (auth.currentUserOrNull() == null) {
            auth.signInAnonymously()
        }
        return auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No se ha podido iniciar sesión en Supabase")
    }

    // Las reglas RLS hacen que cada usuario solo reciba sus propias filas,
    // por eso no hace falta filtrar por usuario_id.
    private suspend fun leerRemoto(): ProgresoUsuario {
        val perfil = cliente.from("perfil")
            .select(Columns.list("usuario_id", "criatura_inicial_id"))
            .decodeSingleOrNull<PerfilFila>()

        val criaturas = cliente.from("usuario_criatura")
            .select(Columns.list("usuario_id", "criatura_id")) {
                order("obtenida_en", Order.DESCENDING)
            }
            .decodeList<CriaturaUsuarioFila>()

        val museos = cliente.from("usuario_museo")
            .select(Columns.list("usuario_id", "museo_id"))
            .decodeList<MuseoUsuarioFila>()

        val obras = cliente.from("usuario_obra")
            .select(Columns.list("usuario_id", "obra_id"))
            .decodeList<ObraUsuarioFila>()

        val misiones = cliente.from("usuario_mision")
            .select(Columns.list("usuario_id", "mision_id"))
            .decodeList<MisionUsuarioFila>()

        return ProgresoUsuario(
            criaturaInicialId = perfil?.criaturaInicialId,
            criaturasDescubiertas = criaturas.map { it.criaturaId },
            museosVisitados = museos.map { it.museoId },
            obrasEscaneadas = obras.map { it.obraId },
            misionesCompletadas = misiones.map { it.misionId }
        )
    }

    // Sube todo el progreso. Las filas que ya existen se ignoran (ignoreDuplicates),
    // así que se puede llamar las veces que haga falta sin duplicar nada.
    private suspend fun subir(usuarioId: String, progreso: ProgresoUsuario) {
        cliente.from("perfil").upsert(
            PerfilFila(usuarioId, progreso.criaturaInicialId)
        )
        if (progreso.criaturasDescubiertas.isNotEmpty()) {
            cliente.from("usuario_criatura").upsert(
                progreso.criaturasDescubiertas.map { CriaturaUsuarioFila(usuarioId, it) }
            ) { ignoreDuplicates = true }
        }
        if (progreso.museosVisitados.isNotEmpty()) {
            cliente.from("usuario_museo").upsert(
                progreso.museosVisitados.map { MuseoUsuarioFila(usuarioId, it) }
            ) { ignoreDuplicates = true }
        }
        if (progreso.obrasEscaneadas.isNotEmpty()) {
            cliente.from("usuario_obra").upsert(
                progreso.obrasEscaneadas.map { ObraUsuarioFila(usuarioId, it) }
            ) { ignoreDuplicates = true }
        }
        if (progreso.misionesCompletadas.isNotEmpty()) {
            cliente.from("usuario_mision").upsert(
                progreso.misionesCompletadas.map { MisionUsuarioFila(usuarioId, it) }
            ) { ignoreDuplicates = true }
        }
    }

    // El progreso solo crece: se queda con todo lo que haya en cualquiera de los dos lados.
    private fun combinar(local: ProgresoUsuario, remoto: ProgresoUsuario): ProgresoUsuario {
        fun unir(a: List<String>, b: List<String>) = a + b.filter { it !in a }
        return ProgresoUsuario(
            criaturaInicialId = local.criaturaInicialId ?: remoto.criaturaInicialId,
            criaturasDescubiertas = unir(local.criaturasDescubiertas, remoto.criaturasDescubiertas),
            museosVisitados = unir(local.museosVisitados, remoto.museosVisitados),
            obrasEscaneadas = unir(local.obrasEscaneadas, remoto.obrasEscaneadas),
            misionesCompletadas = unir(local.misionesCompletadas, remoto.misionesCompletadas)
        )
    }
}

// Filas de las tablas de progreso de supabase/01_esquema.sql

@Serializable
private data class PerfilFila(
    @SerialName("usuario_id") val usuarioId: String,
    @SerialName("criatura_inicial_id") val criaturaInicialId: String? = null
)

@Serializable
private data class CriaturaUsuarioFila(
    @SerialName("usuario_id") val usuarioId: String,
    @SerialName("criatura_id") val criaturaId: String
)

@Serializable
private data class MuseoUsuarioFila(
    @SerialName("usuario_id") val usuarioId: String,
    @SerialName("museo_id") val museoId: String
)

@Serializable
private data class ObraUsuarioFila(
    @SerialName("usuario_id") val usuarioId: String,
    @SerialName("obra_id") val obraId: String
)

@Serializable
private data class MisionUsuarioFila(
    @SerialName("usuario_id") val usuarioId: String,
    @SerialName("mision_id") val misionId: String
)
