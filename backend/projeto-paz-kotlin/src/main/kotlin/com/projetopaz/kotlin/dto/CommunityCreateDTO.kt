package com.projetopaz.kotlin.dto

import java.time.LocalDate

// DTO para criação/atualização
data class CommunityCreateDTO(
    val name: String,
    val description: String,
    val cep: String,
    val quarter: String,
    val number: String,
    val complement: String? = null
)
