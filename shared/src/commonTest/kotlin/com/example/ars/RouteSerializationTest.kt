package com.example.ars

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class RouteSerializationTest {
    @Test
    fun laFichaConservaLasRecompensasDeMision() {
        val destino: Route = Route.DetalleObra("obra_002", null, listOf("criatura_004", "criatura_008"))
        assertEquals(destino, Json.decodeFromString<Route>(Json.encodeToString<Route>(destino)))
    }

    @Test
    fun destinoDeNotificacionConservaElTipoAlPasarPorJson() {
        val destino: Route = Route.Home
        val json = Json.encodeToString<Route>(destino)

        assertEquals(destino, Json.decodeFromString<Route>(json))
    }
}
