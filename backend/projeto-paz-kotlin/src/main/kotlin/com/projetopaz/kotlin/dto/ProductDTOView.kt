package com.projetopaz.kotlin.dto

import java.math.BigDecimal

data class ProductDTOView(
    var id: Long?,

    val name: String,

    val salePrice: BigDecimal,

    val categories: List<String>,

    val favoriteImageUrl: String?
)