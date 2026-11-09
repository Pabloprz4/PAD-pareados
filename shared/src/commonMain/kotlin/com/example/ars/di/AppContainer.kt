
package com.example.ars.di

import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase

import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase

import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.domain.repository.MuseosRepository
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase

interface AppContainer {
    // Home
    val obtenerCriaturasUseCase: ObtenerCriaturasUseCase

    // Escáner
    val validarQrUseCase: ValidarQrUseCase

    // Museos
    val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase
}

class DefaultAppContainer : AppContainer {

    // Repositorio de Inicio
    private val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl()
    }

    override val obtenerCriaturasUseCase: ObtenerCriaturasUseCase by lazy {
        ObtenerCriaturasUseCase(repository = homeRepository)
    }

    // Inicio, Museos y el escáner comparten el catálogo
    private val museosRepository: MuseosRepository by lazy {
        MuseosRepositoryImpl()
    }

    // Repositorio del Escáner
    private val catalogoRepository: CatalogoRepository by lazy {
        CatalogoRepositoryImpl(museosRepository = museosRepository)
    }

    override val validarQrUseCase: ValidarQrUseCase by lazy {
        ValidarQrUseCase(
            interpretarQrUseCase = InterpretarQrUseCase(),
            catalogoRepository = catalogoRepository
        )
    }

    override val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase by lazy {
        ObtenerCatalogoMuseosUseCase(repository = museosRepository)
    }
}
