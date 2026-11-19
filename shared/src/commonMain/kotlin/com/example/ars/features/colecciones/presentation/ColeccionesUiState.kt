package com.example.ars.features.colecciones.presentation

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.ProgresoUsuario

data class ColeccionesUiState(
    val cargando: Boolean = true,
    val criaturas: List<Criatura> = emptyList(),
    val progreso: ProgresoUsuario = ProgresoUsuario(),
    val error: Boolean = false
) {

    val criaturasDescubiertas: List<Criatura>
        get() = progreso.criaturasDescubiertas.mapNotNull { criaturaId ->
            criaturas.firstOrNull { criatura ->
                criatura.id == criaturaId
            }
        }

    val totalCriaturas: Int
        get() = criaturas.size

    val totalDescubiertas: Int
        get() = criaturasDescubiertas.size
}