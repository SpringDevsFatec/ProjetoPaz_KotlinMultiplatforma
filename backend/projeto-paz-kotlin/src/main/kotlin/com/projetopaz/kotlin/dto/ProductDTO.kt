package com.projetopaz.kotlin.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProductDTO(
    val id: Long? = null,

    val name: String,

    val description: String?,

    val costPrice: BigDecimal,

    val salePrice: BigDecimal,

    val isFavorite: Boolean,

    val isDonation: Boolean,

    val categoryIds: List<Long>,

    val supplier: Long?,

    val stock: StockDTO?

)

data class StockDTO(
    val quantity: Int,
    val fabrication: LocalDate?,
    val maturity: LocalDate?
)