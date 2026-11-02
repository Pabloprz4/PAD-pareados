package com.example.ars.features.escaner.presentation

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class LectorQrAndroid(context: Context) {
    private val opciones = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val lector = GmsBarcodeScanning.getClient(context, opciones)

    fun leer(onLeido: (String?) -> Unit, onCancelado: () -> Unit, onError: () -> Unit) {
        try {
            // Google abre el lector y nos devuelve solo el texto del QR
            lector.startScan()
                .addOnSuccessListener { onLeido(it.rawValue) }
                .addOnCanceledListener { onCancelado() }
                .addOnFailureListener { onError() }
        } catch (_: Exception) {
            onError()
        }
    }
}
