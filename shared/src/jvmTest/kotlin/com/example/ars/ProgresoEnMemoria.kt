package com.example.ars

import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.home.domain.repository.ProgresoRepository
import kotlinx.coroutines.yield
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProgresoEnMemoria(var progreso: ProgresoUsuario = ProgresoUsuario()) : ProgresoRepository {
    private val mutex = Mutex()
    var guardados = 0
    var fallaGuardado = false

    override suspend fun obtenerProgreso() = progreso

    override suspend fun guardarProgreso(progreso: ProgresoUsuario) {
        check(!fallaGuardado) { "Fallo al guardar" }
        this.progreso = progreso
        guardados++
    }

    override suspend fun actualizarProgreso(
        cambio: (ProgresoUsuario) -> ProgresoUsuario
    ): ProgresoUsuario = mutex.withLock {
        val anterior = progreso
        yield()
        val nuevo = cambio(anterior)
        if (nuevo != progreso) guardarProgreso(nuevo)
        nuevo
    }
}
