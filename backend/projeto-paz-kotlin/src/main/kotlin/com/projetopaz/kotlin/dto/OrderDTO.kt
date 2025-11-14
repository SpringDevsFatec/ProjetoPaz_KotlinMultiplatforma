package com.projetopaz.kotlin.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class OrderDTO(
    val paymentMethod: String,
    @JsonIgnore
    var total_amount_order: Double? = null,
    val items: List<ItemOrderDTO>
)
