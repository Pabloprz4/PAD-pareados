package com.example.ars.features.escaner.domain.usecase
import com.example.ars.features.escaner.domain.model.CodigoArs

class InterpretarQrUseCase {
    /*
    * 1. Recibe texto que return lector
    * 2. Comprueba if contenido cumple formato
    * 3. Extrae tipo e id
    * 4. Return codigo tipado (o null)
    * */

    private val formato = Regex(
        "ars:v1:(museo|obra):([a-z0-9][a-z0-9_-]{0,63})"
    )

    operator fun invoke(contenido: String?): CodigoArs? {
        if (contenido == null) return null

        // tiene que coincidir todo el texto, no solo contener un código válido
        val coincidencia = formato.matchEntire(contenido)
            ?: return null

        val tipo = coincidencia.groupValues[1]
        val identificador = coincidencia.groupValues[2]

        return when (tipo) {
            "museo" -> CodigoArs.Museo(
                museoId = identificador
            )

            "obra" -> CodigoArs.Obra(
                obraId = identificador
            )

            else -> null
        }
    }
}