package com.projetopaz.frontend_paz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform