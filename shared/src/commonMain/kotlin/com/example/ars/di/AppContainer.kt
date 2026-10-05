package com.example.ars.di

import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase

interface AppContainer {
    val obtenerCriaturasUseCase: ObtenerCriaturasUseCase
}

class DefaultAppContainer : AppContainer {
    private val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl()
    }

    override val obtenerCriaturasUseCase: ObtenerCriaturasUseCase by lazy {
        ObtenerCriaturasUseCase(repository = homeRepository)
    }
}