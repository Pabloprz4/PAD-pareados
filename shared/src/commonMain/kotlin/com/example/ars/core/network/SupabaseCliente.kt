package com.example.ars.core.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

// Datos del proyecto de Supabase.
// La clave "publishable" es pública: está pensada para ir dentro de la app.
// La protección de los datos la hacen las reglas RLS de supabase/01_esquema.sql.
private const val SUPABASE_URL = "https://heguyhuwkltpmjanjztr.supabase.co"
private const val SUPABASE_CLAVE_PUBLICA = "sb_publishable_Besm_axzXo5GVv7Ad89zYA_EFlWtO6x"

// Crea el cliente de Supabase con los dos módulos que usamos:
// Postgrest (consultas a las tablas) y Auth (usuario anónimo para el progreso).
fun crearClienteSupabase(): SupabaseClient =
    createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_CLAVE_PUBLICA
    ) {
        install(Postgrest)
        install(Auth)
    }
