package com.example.ars

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class RouteSerializationTest {
    @Test
    fun destinoDeNotificacionConservaElTipoAlPasarPorJson() {
        val destino: Route = Route.Home
        val json = Json.encodeToString<Route>(destino)

        assertEquals(destino, Json.decodeFromString<Route>(json))
    }
}
