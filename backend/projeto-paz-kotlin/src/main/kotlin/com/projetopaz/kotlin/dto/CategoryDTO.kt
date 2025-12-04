package com.projetopaz.kotlin.dto

data class CategoryDTO(
    val name: String,
    val categoryType: String,

    val description: String? = null,
    val altImage: String? = null,
    val favorite: Boolean = false,

    // Usado SOMENTE no create para subir imagem
    val imgBase64: String? = null
)
