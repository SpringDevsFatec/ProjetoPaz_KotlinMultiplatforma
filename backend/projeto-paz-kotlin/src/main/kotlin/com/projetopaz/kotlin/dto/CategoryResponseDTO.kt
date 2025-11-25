package com.projetopaz.kotlin.dto

data class CategoryResponseDTO(
    val id: Long?,
    val name: String,
    val categoryType: String,
    val imgCategory: String?,
    val altImage: String?,
    val favorite: Boolean,
    val status: Int
)
