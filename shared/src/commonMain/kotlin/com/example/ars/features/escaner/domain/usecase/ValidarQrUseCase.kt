package com.example.ars.features.escaner.domain.usecase

import com.example.ars.features.escaner.domain.model.CodigoArs
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.repository.CatalogoRepository

class ValidarQrUseCase(
    private val interpretarQrUseCase: InterpretarQrUseCase,
    private val catalogoRepository: CatalogoRepository
) {

    suspend operator fun invoke(
        contenido: String?,
        museoActivoId: String?
    ): ResultadoValidacionQr {
        return when (val codigo = interpretarQrUseCase(contenido)) {
            null -> ResultadoValidacionQr.CodigoInvalido

            is CodigoArs.Museo -> {
                val museo = catalogoRepository.obtenerMuseoPorId(
                    id = codigo.museoId
                ) ?: return ResultadoValidacionQr.MuseoDesconocido

                ResultadoValidacionQr.MuseoValido(museo = museo)
            }

            is CodigoArs.Obra -> validarObra(
                obraId = codigo.obraId,
                museoActivoId = museoActivoId
            )
        }
    }

    private suspend fun validarObra(
        obraId: String,
        museoActivoId: String?
    ): ResultadoValidacionQr {
        val obra = catalogoRepository.obtenerObraPorId(
            id = obraId
        ) ?: return ResultadoValidacionQr.ObraDesconocida

        val museo = catalogoRepository.obtenerMuseoPorId(
            id = obra.museoId
        ) ?: return ResultadoValidacionQr.MuseoDesconocido

        return when {
            museoActivoId == null -> {
                ResultadoValidacionQr.RequiereEscanearMuseo
            }

            museoActivoId != museo.id -> {
                ResultadoValidacionQr.MuseoIncorrecto(
                    museoEsperado = museo
                )
            }

            else -> {
                ResultadoValidacionQr.ObraValida(obra = obra)
            }
        }
    }
}