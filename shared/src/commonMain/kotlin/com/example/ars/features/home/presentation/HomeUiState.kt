package com.example.ars.features.home.presentation

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.museos.domain.model.Museo
import org.jetbrains.compose.resources.StringResource

data class HomeUiState(
    val cargando: Boolean = false,
    val museos: List<Museo> = emptyList(),
    val criaturas: List<Criatura> = emptyList(),
    val progreso: ProgresoUsuario = ProgresoUsuario(),
    val errores: List<StringResource> = emptyList()
) {

    val criaturasDescubiertas: List<Criatura>
        get() = progreso.criaturasDescubiertas.mapNotNull { criaturaId ->
            criaturas.firstOrNull { criatura ->
                criatura.id == criaturaId
            }
        }

    val ultimasCriaturas: List<Criatura>
        get() = criaturasDescubiertas.take(5)
}