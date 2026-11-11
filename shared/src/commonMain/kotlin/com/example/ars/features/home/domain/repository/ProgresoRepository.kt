package com.example.ars.features.home.domain.repository

import com.example.ars.features.home.domain.model.ProgresoUsuario

interface ProgresoRepository {

    suspend fun obtenerProgreso(): ProgresoUsuario

    suspend fun guardarProgreso(progreso: ProgresoUsuario)

    suspend fun actualizarProgreso(
        cambio: (ProgresoUsuario) -> ProgresoUsuario
    ): ProgresoUsuario
}