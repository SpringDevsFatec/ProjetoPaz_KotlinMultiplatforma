package com.projetopaz.kotlin.dto

data class OrderDTO(
    val paymentMethod: String,
    var total_amount_order: Double? = null,
    val items: List<ItemOrderDTO>
)
