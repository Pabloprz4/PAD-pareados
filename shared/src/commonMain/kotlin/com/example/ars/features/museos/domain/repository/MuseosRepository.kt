
package com.example.ars.features.museos.domain.repository

import com.example.ars.features.museos.domain.model.Museo

interface MuseosRepository {
    suspend fun obtenerMuseos(): List<Museo>
}
