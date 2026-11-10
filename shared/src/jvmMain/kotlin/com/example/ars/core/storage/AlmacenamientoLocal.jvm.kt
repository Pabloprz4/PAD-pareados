package com.example.ars.core.storage

import java.util.prefs.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val preferencias: Preferences =
    Preferences.userRoot().node("com/example/ars")



//ESTO COMENTADO ES PARA REINICIAR LO DE LA CRIATURA INCIAL!!!!!
/*
actual suspend fun leerTextoLocal(
    clave: String
): String? = withContext(Dispatchers.IO) {
    preferencias.remove(clave)
    preferencias.flush()

    preferencias.get(clave, null)
}
*/


actual suspend fun leerTextoLocal(
    clave: String
): String? = withContext(Dispatchers.IO) {


    preferencias.get(clave, null)
}



actual suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
) {
    withContext(Dispatchers.IO) {
        preferencias.put(clave, contenido)
        preferencias.flush()
    }
}