package com.projetopaz.kotlin.dto

data class OrderResponseDTO(
    val id: Long,
    val paymentMethod: String,
    val total: Double,
    val status: Int,
    val items: List<ItemOrderDTO>
)