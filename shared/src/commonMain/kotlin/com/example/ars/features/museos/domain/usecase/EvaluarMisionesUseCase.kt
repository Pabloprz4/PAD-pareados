package com.example.ars.features.museos.domain.usecase

import com.example.ars.features.escaner.domain.model.Obra
import com.example.ars.features.home.domain.model.ProgresoUsuario
import com.example.ars.features.museos.domain.model.EvaluacionMisiones
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ObjetivoMision
import com.example.ars.features.museos.domain.model.ProgresoMision

class EvaluarMisionesUseCase {
    operator fun invoke(
        museo: Museo,
        obras: List<Obra>,
        progreso: ProgresoUsuario
    ): EvaluacionMisiones {
        val obrasMuseo = obras.filter { it.museoId == museo.id }.map { it.id }.toSet()
        val escaneadas = obrasMuseo.intersect(progreso.obrasEscaneadas.toSet())
        val visitado = museo.id in progreso.museosVisitados
        val configuradas = museo.misiones.filterNot {
            // la misión del starter no hace falta si ya lo elegiste al empezar
            val recompensa = it.recompensaCriaturaId ?: museo.recompensaMisionesId
            recompensa != null && recompensa == progreso.criaturaInicialId
        }
        val recompensas = museo.misiones.mapNotNull { it.recompensaCriaturaId } +
            listOfNotNull(museo.recompensaMisionesId)
        check(recompensas.all { it in museo.criaturasIds }) {
            "Hay una recompensa que no pertenece a este museo."
        }
        val misiones = configuradas.map { mision ->
            val total: Int
            val actual: Int
            val disponible: Boolean
            when (val objetivo = mision.objetivo) {
                is ObjetivoMision.CantidadObras -> {
                    total = objetivo.cantidad
                    actual = escaneadas.size.coerceAtMost(total)
                    disponible = obrasMuseo.size >= total
                }
                is ObjetivoMision.ObrasConcretas -> {
                    total = objetivo.ids.size
                    actual = objetivo.ids.count { it in escaneadas }
                    disponible = objetivo.ids.all { it in obrasMuseo }
                }
            }
            val completada = mision.id in progreso.misionesCompletadas ||
                (visitado && disponible && actual == total)
            ProgresoMision(mision, if (completada) total else actual, total, completada, disponible)
        }
        if (!visitado) return EvaluacionMisiones(progreso, misiones)

        val completadas = misiones.filter { it.completada }
        val premios = completadas.mapNotNull { it.mision.recompensaCriaturaId }.toMutableList()
        // algunos museos dan una sola criatura al completar todas sus misiones
        if (misiones.isNotEmpty() && misiones.all { it.completada }) {
            museo.recompensaMisionesId?.let { premios.add(it) }
        }
        val nuevas = premios.distinct().filterNot { it in progreso.criaturasDescubiertas }
        val nuevo = progreso.copy(
            misionesCompletadas = (progreso.misionesCompletadas + completadas.map { it.mision.id }).distinct(),
            criaturasDescubiertas = nuevas + progreso.criaturasDescubiertas
        )
        return EvaluacionMisiones(nuevo, misiones)
    }
}
