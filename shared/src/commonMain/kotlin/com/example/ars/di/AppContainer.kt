package com.example.ars.di

import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerMuseosUseCase

interface AppContainer {
    val obtenerCriaturasUseCase: ObtenerCriaturasUseCase
    val obtenerMuseosUseCase: ObtenerMuseosUseCase
}

class DefaultAppContainer : AppContainer {
    private val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl()
    }

    override val obtenerCriaturasUseCase: ObtenerCriaturasUseCase by lazy {
        ObtenerCriaturasUseCase(repository = homeRepository)
    }
    override val obtenerMuseosUseCase: ObtenerMuseosUseCase by lazy {
        ObtenerMuseosUseCase(repository = homeRepository)
    }
}