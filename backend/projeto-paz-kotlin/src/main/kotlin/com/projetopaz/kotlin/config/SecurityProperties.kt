package com.projetopaz.kotlin.config



data class SecurityProperties(
    val tokenSecretKey: String,
    val tokenExpirationSeconds: Long
)
