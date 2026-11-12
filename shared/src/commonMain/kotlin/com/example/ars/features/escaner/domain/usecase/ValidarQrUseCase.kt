package com.example.ars.features.escaner.domain.usecase

import com.example.ars.features.escaner.domain.model.CodigoArs
import com.example.ars.features.escaner.domain.model.ResultadoValidacionQr
import com.example.ars.features.escaner.domain.repository.CatalogoRepository

class ValidarQrUseCase(
    private val interpretarQrUseCase: InterpretarQrUseCase,
    private val catalogoRepository: CatalogoRepository
) {
    suspend operator fun invoke(contenido: String?): ResultadoValidacionQr {
        // primero comprobamos el formato, después si existe en el catálogo
        return when (val codigo = interpretarQrUseCase(contenido)) {
            null -> ResultadoValidacionQr.CodigoInvalido
            is CodigoArs.Museo -> {
                val museo = catalogoRepository.obtenerMuseoPorId(codigo.museoId)
                    ?: return ResultadoValidacionQr.MuseoDesconocido
                ResultadoValidacionQr.MuseoValido(museo)
            }
            is CodigoArs.Obra -> {
                val obra = catalogoRepository.obtenerObraPorId(codigo.obraId)
                    ?: return ResultadoValidacionQr.ObraDesconocida
                val museo = catalogoRepository.obtenerMuseoPorId(obra.museoId)
                    ?: return ResultadoValidacionQr.MuseoDesconocido
                ResultadoValidacionQr.ObraValida(obra, museo)
            }
        }
    }
}
