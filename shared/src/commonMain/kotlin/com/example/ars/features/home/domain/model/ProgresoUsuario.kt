package com.example.ars.features.home.domain.model

import kotlinx.serialization.Serializable
import com.example.ars.features.museos.domain.model.Museo

@Serializable
data class ProgresoUsuario(
    val criaturaInicialId: String? = null,
    val criaturasDescubiertas: List<String> = emptyList(),
    val museosVisitados: List<String> = emptyList(),
    val obrasEscaneadas: List<String> = emptyList(),
    val misionesCompletadas: List<String> = emptyList()
) {
    fun conVisita(museo: Museo): ProgresoUsuario {
        if (museo.id in museosVisitados) return this
        val bienvenida = museo.criaturaBienvenidaId?.takeIf {
            it !in criaturasDescubiertas
        }
        return copy(
            museosVisitados = museosVisitados + museo.id,
            criaturasDescubiertas = listOfNotNull(bienvenida) + criaturasDescubiertas
        )
    }
}
