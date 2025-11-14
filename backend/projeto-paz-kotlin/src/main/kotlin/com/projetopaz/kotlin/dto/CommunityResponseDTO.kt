package com.projetopaz.kotlin.dto

import java.time.LocalDate

// DTO de resposta
data class CommunityResponseDTO(
    val id: Long,
    val name: String,
    val description: String,
    val cep: String,
    val quarter: String,
    val number: String,
    val complement: String?,
    val status: Boolean,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
