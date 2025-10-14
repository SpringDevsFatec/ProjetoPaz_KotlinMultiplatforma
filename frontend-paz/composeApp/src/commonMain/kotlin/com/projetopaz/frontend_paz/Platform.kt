package com.projetopaz.frontend_paz

enum class Platform {
    Android, IOS, Desktop, Web
}

expect fun getPlatform(): Platform