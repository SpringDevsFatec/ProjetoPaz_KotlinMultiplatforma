package com.projetopaz.kotlin.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProductDTO(
    var id: Long?,

    val name: String,

    val description: String?,

    val costPrice: BigDecimal,

    val salePrice: BigDecimal,

    val isFavorite: Boolean,

    val isDonation: Boolean,

    //val categories: List<Long>,

    val supplier: Long?,

    val images: List<ProductImageDTO>,

    val stock: StockDTO?

) {
    constructor():this(id=null, name="", description=null,
        costPrice=BigDecimal.ONE, salePrice=BigDecimal.ONE,
        isFavorite=false, isDonation=false,
        supplier=null, images=emptyList(), stock=null
    )
}

data class StockDTO(
    val quantity: Int,
    val fabrication: LocalDate?,
    val maturity: LocalDate?
)