
package com.example.ars.features.museos.presentation

sealed interface MuseosAction {
    data object CargarMuseos : MuseosAction
}
