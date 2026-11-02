package com.example.ars.notificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ars.MainActivity
import com.example.ars.R

internal const val EXTRA_TO_SCREEN = "toScreen"

class NotificacionWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val titulo = inputData.getString("title") ?: return Result.failure()
        val mensaje = inputData.getString("message") ?: return Result.failure()
        val rutaJson = inputData.getString("route") ?: return Result.failure()

        mostrarNotificacion(titulo, mensaje, rutaJson)
        return Result.success()
    }

    private fun crearCanalNotificaciones(gestor: NotificationManager) {
        val canal = NotificationChannel(
            CANAL_RECORDATORIOS,
            applicationContext.getString(R.string.recordatorios_canal_nombre),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = applicationContext.getString(R.string.recordatorios_canal_descripcion)
        }
        gestor.createNotificationChannel(canal)
    }

    private fun mostrarNotificacion(titulo: String, mensaje: String, rutaJson: String) {
        val gestor = applicationContext.getSystemService(NotificationManager::class.java)
        crearCanalNotificaciones(gestor)

        val notificacionId = System.currentTimeMillis().toInt()
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_TO_SCREEN, rutaJson)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificacionId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificacion = NotificationCompat.Builder(applicationContext, CANAL_RECORDATORIOS)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // El permiso puede haberse revocado después de programar el trabajo.
        if (applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return

        gestor.notify(notificacionId, notificacion)
    }

    private companion object {
        const val CANAL_RECORDATORIOS = "ars_recordatorios"
    }
}
