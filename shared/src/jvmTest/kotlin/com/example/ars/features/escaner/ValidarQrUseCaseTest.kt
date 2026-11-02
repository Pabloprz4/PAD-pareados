package com.example.ars.features.escaner

import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.escaner.domain.model.CodigoArs
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ValidarQrUseCaseTest {
    private val validar = DefaultAppContainer().validarQrUseCase

    @Test
    fun interpretaSoloElFormatoCompletoDeArs() {
        val interpretar = InterpretarQrUseCase()
        assertEquals(CodigoArs.Museo("museo_001"), interpretar("ars:v1:museo:museo_001"))
        assertEquals(CodigoArs.Obra("obra_001"), interpretar("ars:v1:obra:obra_001"))
        listOf(null, "", "https://ejemplo.com", "ars:v2:obra:obra_001", "ars:v1:obra:",
            "texto ars:v1:obra:obra_001", "ars:v1:obra:obra_001 extra").forEach {
            assertNull(interpretar(it))
        }
    }

    @Test
    fun identificaMuseosYRechazaLosDesconocidos() = runBlocking {
        val resultado = assertIs<ResultadoValidacionQr.MuseoValido>(
            validar("ars:v1:museo:museo_001", museoActivoId = null)
        )
        assertEquals("Museo del Prado", resultado.museo.nombre)
        assertEquals(ResultadoValidacionQr.MuseoDesconocido,
            validar("ars:v1:museo:desconocido", museoActivoId = null))
        assertEquals(ResultadoValidacionQr.CodigoInvalido, validar("", museoActivoId = null))
    }

    @Test
    fun unaObraNecesitaElMuseoCorrecto() = runBlocking {
        val contenido = "ars:v1:obra:obra_001"
        assertEquals(ResultadoValidacionQr.RequiereEscanearMuseo, validar(contenido, null))
        val incorrecto = assertIs<ResultadoValidacionQr.MuseoIncorrecto>(
            validar(contenido, "museo_002")
        )
        assertEquals("museo_001", incorrecto.museoEsperado.id)
        val correcto = assertIs<ResultadoValidacionQr.ObraValida>(validar(contenido, "museo_001"))
        assertEquals("Las meninas", correcto.obra.titulo)
        assertEquals(ResultadoValidacionQr.ObraDesconocida,
            validar("ars:v1:obra:desconocida", "museo_001"))
    }
}
