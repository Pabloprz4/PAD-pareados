package com.example.ars.features.home.data.repository

import com.example.ars.core.storage.guardarTextoLocal
import com.example.ars.core.storage.leerTextoLocal
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//convierte y lee JSON del progreso de cada usuario

class ProgresoRepositoryImpl : ProgresoRepository {

    private val clave = "ars_progreso_usuario"
    private companion object {
        val mutex = Mutex()
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun obtenerProgreso(): ProgresoUsuario {
        val contenido = leerTextoLocal(clave)
            ?: return ProgresoUsuario()

        return json.decodeFromString<ProgresoUsuario>(contenido)
    }

    override suspend fun guardarProgreso(
        progreso: ProgresoUsuario
    ) {
        val contenido = json.encodeToString(progreso)

        guardarTextoLocal(
            clave = clave,
            contenido = contenido
        )
    }

    override suspend fun actualizarProgreso(
        cambio: (ProgresoUsuario) -> ProgresoUsuario
    ): ProgresoUsuario = mutex.withLock {
        // el starter y las visitas se guardan de uno en uno para no pisarse
        val anterior = obtenerProgreso()
        val nuevo = cambio(anterior)
        if (nuevo != anterior) {
            guardarProgreso(nuevo)
        }
        nuevo
    }
}
