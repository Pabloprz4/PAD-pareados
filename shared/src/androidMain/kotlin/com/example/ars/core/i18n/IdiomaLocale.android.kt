package com.example.ars.core.i18n

import android.os.LocaleList

actual fun cambiarIdiomaLocale(idioma: String) {
    LocaleList.setDefault(LocaleList.forLanguageTags(idioma))
}
