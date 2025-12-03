package com.projetopaz.kotlin.dto

data class UserResetPasswordDTO(
    val email: String,
    val token: String,
    val newPassword: String
)