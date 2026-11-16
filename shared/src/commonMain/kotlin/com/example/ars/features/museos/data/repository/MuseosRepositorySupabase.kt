package com.example.ars.features.museos.data.repository

import com.example.ars.core.network.CatalogoRemoto
import com.example.ars.core.network.MisionDto
import com.example.ars.features.museos.domain.model.Mision
import com.example.ars.features.museos.domain.model.Museo
import com.example.ars.features.museos.domain.model.ObjetivoMision
import com.example.ars.features.museos.domain.repository.MuseosRepository
import kotlin.coroutines.cancellation.CancellationException

// Museos y misiones leídos de Supabase.
// Si no hay conexión se usan los datos locales (MuseosRepositoryImpl).
class MuseosRepositorySupabase(
    private val catalogoRemoto: CatalogoRemoto,
    private val respaldoLocal: MuseosRepository
) : MuseosRepository {

    override suspend fun obtenerMuseos(): List<Museo> {
        return try {
            val museos = catalogoRemoto.obtenerMuseos()
            val criaturas = catalogoRemoto.obtenerCriaturas()
            val misiones = catalogoRemoto.obtenerMisiones()

            museos.map { museo ->
                Museo(
                    id = museo.id,
                    nombre = museo.nombre,
                    ciudad = museo.ciudad,
                    pais = museo.pais,
                    // los contadores los calcula ObtenerCatalogoMuseosUseCase con el progreso
                    obrasDescubiertas = 0,
                    obrasTotales = 0,
                    criaturasDescubiertas = 0,
                    criaturasTotales = 0,
                    destacado = museo.destacado,
                    descripcion = museo.descripcion,
                    misiones = misiones
                        .filter { it.museoId == museo.id }
                        .mapNotNull { it.aDominio() },
                    recompensaMisionesId = museo.recompensaMisionesId,
                    criaturasIds = criaturas
                        .filter { it.museoId == museo.id }
                        .map { it.id },
                    criaturaBienvenidaId = museo.criaturaBienvenidaId
                )
            }.ifEmpty { respaldoLocal.obtenerMuseos() }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            respaldoLocal.obtenerMuseos()
        }
    }

    private fun MisionDto.aDominio(): Mision? {
        val objetivo = when (tipo) {
            "CANTIDAD_OBRAS" -> cantidadObras?.let { ObjetivoMision.CantidadObras(it) }
            "OBRAS_CONCRETAS" -> obras.map { it.obraId }
                .takeIf { it.size in 1..4 }
                ?.let { ObjetivoMision.ObrasConcretas(it) }
            else -> null
        } ?: return null // misión mal definida en la base de datos: se ignora

        return Mision(
            id = id,
            titulo = titulo,
            objetivo = objetivo,
            recompensaCriaturaId = recompensaCriaturaId
        )
    }
}
