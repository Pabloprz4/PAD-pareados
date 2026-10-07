package com.example.ars.features.home.domain.repository

import com.example.ars.features.home.domain.model.Criatura
import com.example.ars.features.home.domain.model.Museo
interface HomeRepository {
    suspend fun traerCriaturas(): List<Criatura>
    suspend fun traerMuseos(): List<Museo>
}