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
import com.example.ars.notificaciones.EXTRA_TO_SCREEN
import com.example.ars.notificaciones.ProgramadorNotificaciones
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private var toScreen by mutableStateOf<Route?>(null)

    private val solicitarPermiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // Android guarda la decisión; el Worker comprueba el permiso antes de publicar.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        toScreen = jsonToRoute(intent)
        setContent {
            App(
                toScreen = toScreen,
                onDestinoAbierto = {
                    toScreen = null
                    intent?.removeExtra(EXTRA_TO_SCREEN)
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
        if (!isChangingConfigurations &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            ProgramadorNotificaciones.programarNotificacion(
                context = applicationContext,
                titulo = getString(R.string.recordatorio_titulo),
                mensaje = getString(R.string.recordatorio_mensaje),
                demoraSegundos = 5L, // Demostración; no es el plazo definitivo del producto.
                destino = Route.Home
            )
        }
    }

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
