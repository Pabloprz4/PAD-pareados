package com.example.ars.core.storage

import platform.Foundation.NSUserDefaults

actual suspend fun leerTextoLocal(
    clave: String
): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(clave)
}

actual suspend fun guardarTextoLocal(
    clave: String,
    contenido: String
) {
    NSUserDefaults.standardUserDefaults.setObject(
        contenido,
        forKey = clave
    )
}