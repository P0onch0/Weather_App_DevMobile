package com.legaut.devmobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform