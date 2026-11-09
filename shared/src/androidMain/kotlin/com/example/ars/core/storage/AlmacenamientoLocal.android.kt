package com.example.ars.core.storage

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

private lateinit var preferencias: SharedPreferences

fun inicializarAlmacenamientoLocal(context: Context) {
    preferencias = context.applicationContext.getSharedPreferences(
        "ars_progreso",
        Context.MODE_PRIVATE
    )
}

actual suspend fun leerTextoLocal(
    clave: String
): String? = withContext(Dispatchers.IO) {
    preferencias.getString(clave, null)
}

actual suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
) {
    withContext(Dispatchers.IO) {
        val guardado = preferencias.edit()
            .putString(clave, contenido)
            .commit()

        if (!guardado) {
            throw IOException("No se han podido guardar los datos")
        }
    }
}