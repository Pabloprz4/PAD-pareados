package com.example.ars.notificaciones

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.ars.MainActivity
import com.example.ars.Route
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NotificacionesTest {
    private val context: Application
        get() = RuntimeEnvironment.getApplication()

    @Before
    fun concederPermiso() {
        shadowOf(context).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    @Test
    fun workerPublicaUnAvisoQueAbreHome() = runBlocking {
        val resultado = crearWorker(datosValidos()).doWork()
        val gestor = context.getSystemService(NotificationManager::class.java)
        val notificacion = gestor.activeNotifications.single().notification
        val intent = shadowOf(notificacion.contentIntent).savedIntent
        val rutaJson = intent.getStringExtra(EXTRA_TO_SCREEN)
            ?: error("La notificación debe transportar su destino")

        assertEquals(ListenableWorker.Result.success(), resultado)
        assertNotNull(gestor.getNotificationChannel(notificacion.channelId))
        assertEquals(MainActivity::class.java.name, intent.component?.className)
        assertEquals(Route.Home, Json.decodeFromString<Route>(rutaJson))
    }

    @Test
    fun permisoRevocadoAntesDeEjecutarNoPublicaElAviso() = runBlocking {
        val worker = crearWorker(datosValidos())
        shadowOf(context).denyPermissions(Manifest.permission.POST_NOTIFICATIONS)

        assertEquals(ListenableWorker.Result.success(), worker.doWork())
        assertTrue(context.getSystemService(NotificationManager::class.java)
            .activeNotifications.isEmpty())
    }

    @Test
    fun datosAusentesNoPublicanUnAvisoIncompleto() = runBlocking {
        assertEquals(ListenableWorker.Result.failure(), crearWorker(Data.EMPTY).doWork())
        assertTrue(context.getSystemService(NotificationManager::class.java)
            .activeNotifications.isEmpty())
    }

    private fun datosValidos(): Data = Data.Builder()
        .putString("title", "Ars")
        .putString("message", "Descubre arte")
        .putString("route", Json.encodeToString<Route>(Route.Home))
        .build()

    private fun crearWorker(datos: Data): NotificacionWorker =
        TestListenableWorkerBuilder<NotificacionWorker>(context)
            .setInputData(datos)
            .build()
}
