package com.example.ars.features.escaner

import com.example.ars.di.DefaultAppContainer
import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.escaner.domain.model.CodigoArs
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ValidarQrUseCaseTest {
    private val contenedor = DefaultAppContainer()
    private val validar = contenedor.validarQrUseCase

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
        val museos = MuseosRepositoryImpl().obtenerMuseos()
        assertEquals(10, museos.size)
        for (museo in museos) {
            val resultado = validar("ars:v1:museo:${museo.id}")
            assertEquals(ResultadoValidacionQr.MuseoValido(museo), resultado)
        }
        assertEquals(ResultadoValidacionQr.MuseoDesconocido,
            validar("ars:v1:museo:desconocido"))
        assertEquals(ResultadoValidacionQr.CodigoInvalido, validar(""))
    }

    @Test
    fun identificaLaObraYSuMuseoSinExigirUnQrPrevio() = runBlocking {
        val correcto = assertIs<ResultadoValidacionQr.ObraValida>(validar("ars:v1:obra:obra_001"))
        assertEquals("Las meninas", correcto.obra.titulo)
        assertEquals("museo_001", correcto.museo.id)
        assertEquals(ResultadoValidacionQr.ObraDesconocida, validar("ars:v1:obra:desconocida"))
    }
}
