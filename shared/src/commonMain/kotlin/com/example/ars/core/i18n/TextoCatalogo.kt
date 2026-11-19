package com.example.ars.core.i18n

import androidx.compose.runtime.Composable
import ars.shared.generated.resources.Res
import ars.shared.generated.resources.allStringResources
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun textoCatalogo(clave: String, original: String): String {
    if (LocalIdioma.current != "en") return original
    val traduccion = Res.allStringResources[clave] ?: return original
    return stringResource(traduccion)
}
