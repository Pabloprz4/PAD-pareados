package com.example.ars.features.escaner.presentation

sealed interface EscanerAction {
    data object LeerQr : EscanerAction
    data class QrLeido(val contenido: String?) : EscanerAction
    data object LecturaCancelada : EscanerAction
    data object LecturaFallida : EscanerAction
    data object MuseoAbierto : EscanerAction
}
