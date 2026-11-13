package com.example.ars

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Escaner : Route

    @Serializable
    data object Museos : Route

    @Serializable
    data class DetalleMuseo(
        val museoId: String,
        val yaVisitado: Boolean? = null,
        val criaturaObtenidaId: String? = null,
        val recompensasIds: List<String> = emptyList()
    ) : Route

    @Serializable
    data object Ajustes : Route

    @Serializable
    data class DetalleObra(
        val obraId: String,
        val criaturaBienvenidaId: String? = null,
        val recompensasIds: List<String> = emptyList()
    ) : Route

    @Serializable
    data object PrimeraCriatura : Route



}
