package com.example.ars.di

import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase
import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase
import com.example.ars.features.home.domain.usecase.ObtenerMuseosUseCase

interface AppContainer {
    val obtenerCriaturasUseCase: ObtenerCriaturasUseCase
    val obtenerMuseosUseCase: ObtenerMuseosUseCase
    val validarQrUseCase: ValidarQrUseCase
}

class DefaultAppContainer : AppContainer {
    // inicio y el escáner usan la misma lista de museos
    private val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl()
    }

    override val obtenerCriaturasUseCase: ObtenerCriaturasUseCase by lazy {
        ObtenerCriaturasUseCase(repository = homeRepository)
    }
    override val obtenerMuseosUseCase: ObtenerMuseosUseCase by lazy {
        ObtenerMuseosUseCase(repository = homeRepository)
    }

    private val catalogoRepository: CatalogoRepository by lazy {
        CatalogoRepositoryImpl(homeRepository = homeRepository)
    }

    override val validarQrUseCase: ValidarQrUseCase by lazy {
        ValidarQrUseCase(
            interpretarQrUseCase = InterpretarQrUseCase(),
            catalogoRepository = catalogoRepository
        )
    }
}
