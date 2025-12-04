package com.projetopaz.kotlin.dto

data class ProductCreateDTO(
    val name: String,
    val costPrice: Double,
    val salePrice: Double,
    val description: String? = null,
    val isFavorite: Boolean = false,
    val donation: Boolean = false,
    val supplierId: Long? = null,
    val stock: StockDTO? = null,
    val categoryIds: List<Long> = listOf()
    // Note: imagens são enviadas por endpoint separado /api/imgProduct/{productId}
)
