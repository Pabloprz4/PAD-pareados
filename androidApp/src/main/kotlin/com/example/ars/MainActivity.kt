package com.example.ars

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.ars.features.escaner.presentation.LectorQrAndroid
import com.example.ars.notificaciones.EXTRA_TO_SCREEN
import com.example.ars.notificaciones.ProgramadorNotificaciones
import kotlinx.serialization.json.Json
import com.example.ars.core.storage.inicializarAlmacenamientoLocal

class MainActivity : ComponentActivity() {
    private var toScreen by mutableStateOf<Route?>(null)
    private val lectorQr by lazy { LectorQrAndroid(this) }

    private val solicitarPermiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // Android guarda la decisión; el Worker comprueba el permiso antes de publicar.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        //Inicializamos lo que tenemos guardado
        inicializarAlmacenamientoLocal(applicationContext)

        toScreen = jsonToRoute(intent) // si la app estaba cerrada, el destino llega por aquí
        setContent {
            App(
                toScreen = toScreen,
                onDestinoAbierto = {
                    toScreen = null
                    intent?.removeExtra(EXTRA_TO_SCREEN)
                },
                onLeerQr = lectorQr::leer,
                onModoOscuroCambiado = { oscuro ->
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = !oscuro
                        isAppearanceLightNavigationBars = !oscuro
                    }
                }
            )
        }
        if (savedInstanceState == null &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            solicitarPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onStop() {
        super.onStop()
        // programamos al salir de la app, pero no por girar la pantalla
        if (!isChangingConfigurations &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            ProgramadorNotificaciones.programarNotificacion(
                context = applicationContext,
                titulo = getString(R.string.recordatorio_titulo),
                mensaje = getString(R.string.recordatorio_mensaje),
                demoraSegundos = 5L, // 5s solo para la prueba
                destino = Route.Home
            )
        }
    }

    // si la actividad ya estaba abierta, el nuevo aviso llega aquí
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        toScreen = jsonToRoute(intent)
    }

    private fun jsonToRoute(intent: Intent?): Route? =
        intent?.getStringExtra(EXTRA_TO_SCREEN)?.let { json ->
            runCatching { Json.decodeFromString<Route>(json) }.getOrNull()
        }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
