package com.example.ars.notificaciones

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ars.Route
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json

object ProgramadorNotificaciones {
    fun programarNotificacion(
        context: Context,
        titulo: String,
        mensaje: String,
        demoraSegundos: Long,
        destino: Route = Route.Home
    ) {
        val datos = Data.Builder()
            .putString("title", titulo)
            .putString("message", mensaje)
            .putString("route", Json.encodeToString<Route>(destino))
            .build()
        val trabajo = OneTimeWorkRequestBuilder<NotificacionWorker>()
            .setInitialDelay(demoraSegundos, TimeUnit.SECONDS)
            .setInputData(datos)
            .build()

        WorkManager.getInstance(context.applicationContext).enqueue(trabajo)
    }
}
