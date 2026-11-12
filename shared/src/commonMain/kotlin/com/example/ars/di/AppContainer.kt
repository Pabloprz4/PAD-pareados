
package com.example.ars.di

import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase
import com.example.ars.features.escaner.domain.usecase.RegistrarObraUseCase
import com.example.ars.features.escaner.domain.usecase.ObtenerObrasUseCase
import com.example.ars.features.museos.domain.usecase.RegistrarVisitaMuseoUseCase

import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase

import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.domain.repository.MuseosRepository
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase

import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.home.data.repository.ProgresoRepositoryImpl

import com.example.ars.features.home.domain.usecase.ObtenerProgresoUseCase
import com.example.ars.features.home.domain.usecase.ElegirCriaturaInicialUseCase

interface AppContainer {
    // Home
    val obtenerCriaturasUseCase: ObtenerCriaturasUseCase

    // Escáner
    val validarQrUseCase: ValidarQrUseCase
    val registrarObraUseCase: RegistrarObraUseCase
    val obtenerObrasUseCase: ObtenerObrasUseCase
    val registrarVisitaMuseoUseCase: RegistrarVisitaMuseoUseCase

    // Museos
    val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase

    //Progresos
    val progresoRepository: ProgresoRepository

    //Criaturas iniciales
    val obtenerProgresoUseCase: ObtenerProgresoUseCase
    val elegirCriaturaInicialUseCase: ElegirCriaturaInicialUseCase
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

    override val registrarObraUseCase: RegistrarObraUseCase by lazy {
        RegistrarObraUseCase(progresoRepository, homeRepository)
    }

    override val obtenerObrasUseCase: ObtenerObrasUseCase by lazy {
        ObtenerObrasUseCase(catalogoRepository)
    }

    override val registrarVisitaMuseoUseCase: RegistrarVisitaMuseoUseCase by lazy {
        RegistrarVisitaMuseoUseCase(progresoRepository, homeRepository)
    }

    override val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase by lazy {
        ObtenerCatalogoMuseosUseCase(museosRepository, catalogoRepository, progresoRepository)
    }


    //Progresos
    override val progresoRepository: ProgresoRepository by lazy {
        ProgresoRepositoryImpl()
    }

    //Criaturas iniciales
    override val obtenerProgresoUseCase: ObtenerProgresoUseCase by lazy {
        ObtenerProgresoUseCase(
            repository = progresoRepository
        )
    }

    override val elegirCriaturaInicialUseCase: ElegirCriaturaInicialUseCase by lazy {
        ElegirCriaturaInicialUseCase(
            repository = progresoRepository
        )
    }
}
