package com.example.ars.features.home.domain.repository

import com.example.ars.features.home.domain.model.Criatura

interface HomeRepository {
    suspend fun traerCriaturas(): List<Criatura>
}