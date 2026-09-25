package com.example.ars

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform