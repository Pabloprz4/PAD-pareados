package com.example.ars.features.cuenta.data

import com.example.ars.features.cuenta.domain.CuentaRepository
import com.example.ars.features.cuenta.domain.ErrorCuenta
import com.example.ars.features.cuenta.domain.FalloCuenta
import com.example.ars.features.cuenta.domain.SesionCuenta
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class CuentaRepositorySupabase(cliente: SupabaseClient) : CuentaRepository {
    private val auth = cliente.auth

    override val sesion = auth.sessionStatus.map { estado ->
        when (estado) {
            SessionStatus.Initializing -> SesionCuenta.Cargando
            is SessionStatus.Authenticated -> {
                val usuario = estado.session.user
                // Una sesión anónima antigua sigue siendo una partida de invitado.
                val correo = usuario?.email
                if (usuario != null && usuario.isAnonymous != true && !correo.isNullOrBlank()) {
                    SesionCuenta.Activa(usuario.id, correo)
                } else {
                    SesionCuenta.SinCuenta
                }
            }
            else -> SesionCuenta.SinCuenta
        }
    }.distinctUntilChanged()

    override suspend fun entrar(correo: String, clave: String) {
        try {
            auth.signInWith(Email) {
                email = correo
                password = clave
            }
        } catch (error: AuthRestException) {
            throw traducir(error)
        }
    }

    override suspend fun registrar(correo: String, clave: String): Boolean {
        try {
            return auth.signUpWith(Email) {
                email = correo
                password = clave
            } != null
        } catch (error: AuthRestException) {
            throw traducir(error)
        }
    }

    override suspend fun salir() {
        // Las sesiones del mismo usuario en otros dispositivos siguen abiertas.
        auth.signOut(SignOutScope.LOCAL)
    }

    private fun traducir(error: AuthRestException): FalloCuenta {
        return FalloCuenta(when (error.errorCode) {
            AuthErrorCode.InvalidCredentials -> ErrorCuenta.Credenciales
            AuthErrorCode.EmailNotConfirmed -> ErrorCuenta.CorreoSinConfirmar
            AuthErrorCode.UserAlreadyExists, AuthErrorCode.EmailExists -> ErrorCuenta.CorreoYaRegistrado
            AuthErrorCode.EmailAddressNotAuthorized -> ErrorCuenta.CorreoNoAutorizado
            AuthErrorCode.OverEmailSendRateLimit, AuthErrorCode.OverRequestRateLimit -> ErrorCuenta.DemasiadosIntentos
            else -> ErrorCuenta.Otro
        })
    }
}
