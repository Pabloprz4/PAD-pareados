
package com.example.ars.di

import com.example.ars.core.network.CatalogoRemoto
import com.example.ars.core.network.crearClienteSupabase
import com.example.ars.features.escaner.data.repository.CatalogoRepositoryImpl
import com.example.ars.features.escaner.data.repository.CatalogoRepositorySupabase
import com.example.ars.features.escaner.domain.repository.CatalogoRepository
import com.example.ars.features.escaner.domain.usecase.InterpretarQrUseCase
import com.example.ars.features.escaner.domain.usecase.ValidarQrUseCase
import com.example.ars.features.escaner.domain.usecase.RegistrarObraUseCase
import com.example.ars.features.escaner.domain.usecase.ObtenerObrasUseCase
import com.example.ars.features.museos.domain.usecase.RegistrarVisitaMuseoUseCase

import com.example.ars.features.home.data.repository.HomeRepositoryImpl
import com.example.ars.features.home.data.repository.HomeRepositorySupabase
import com.example.ars.features.home.domain.repository.HomeRepository
import com.example.ars.features.home.domain.usecase.ObtenerCriaturasUseCase

import com.example.ars.features.museos.data.repository.MuseosRepositoryImpl
import com.example.ars.features.museos.data.repository.MuseosRepositorySupabase
import com.example.ars.features.museos.domain.repository.MuseosRepository
import com.example.ars.features.museos.domain.usecase.ObtenerCatalogoMuseosUseCase

import com.example.ars.features.home.domain.repository.ProgresoRepository
import com.example.ars.features.home.data.repository.ProgresoRepositoryImpl
import com.example.ars.features.home.data.repository.ProgresoRepositorySupabase

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
    // Supabase: un único cliente y un único catálogo para toda la app
    private val supabase by lazy {
        crearClienteSupabase()
    }

    private val catalogoRemoto by lazy {
        CatalogoRemoto(supabase)
    }

    private val homeRepository: HomeRepository by lazy {
        // si Supabase no responde se usan los datos locales
        HomeRepositorySupabase(catalogoRemoto, respaldoLocal = HomeRepositoryImpl())
    }

    override val obtenerCriaturasUseCase: ObtenerCriaturasUseCase by lazy {
        ObtenerCriaturasUseCase(repository = homeRepository)
    }

    // Inicio, Museos y el escáner comparten el catálogo
    private val museosRepository: MuseosRepository by lazy {
        MuseosRepositorySupabase(catalogoRemoto, respaldoLocal = MuseosRepositoryImpl())
    }

    // Repositorio del Escáner
    private val catalogoRepository: CatalogoRepository by lazy {
        CatalogoRepositorySupabase(
            catalogoRemoto = catalogoRemoto,
            museosRepository = museosRepository,
            respaldoLocal = CatalogoRepositoryImpl(museosRepository = museosRepository)
        )
    }

    override val validarQrUseCase: ValidarQrUseCase by lazy {
        ValidarQrUseCase(
            interpretarQrUseCase = InterpretarQrUseCase(),
            catalogoRepository = catalogoRepository
        )
    }

    override val registrarObraUseCase: RegistrarObraUseCase by lazy {
        RegistrarObraUseCase(progresoRepository, homeRepository, catalogoRepository)
    }

    override val obtenerObrasUseCase: ObtenerObrasUseCase by lazy {
        ObtenerObrasUseCase(catalogoRepository)
    }

    override val registrarVisitaMuseoUseCase: RegistrarVisitaMuseoUseCase by lazy {
        RegistrarVisitaMuseoUseCase(progresoRepository, homeRepository, catalogoRepository)
    }

    override val obtenerCatalogoMuseosUseCase: ObtenerCatalogoMuseosUseCase by lazy {
        ObtenerCatalogoMuseosUseCase(museosRepository, catalogoRepository, obtenerProgresoUseCase)
    }


    //Progresos
    override val progresoRepository: ProgresoRepository by lazy {
        // se guarda en local y se sincroniza con Supabase
        ProgresoRepositorySupabase(supabase, local = ProgresoRepositoryImpl())
    }

    //Criaturas iniciales
    override val obtenerProgresoUseCase: ObtenerProgresoUseCase by lazy {
        ObtenerProgresoUseCase(
            repository = progresoRepository,
            museosRepository = museosRepository,
            catalogoRepository = catalogoRepository
        )
    }

    override val elegirCriaturaInicialUseCase: ElegirCriaturaInicialUseCase by lazy {
        ElegirCriaturaInicialUseCase(
            repository = progresoRepository
        )
    }
}
