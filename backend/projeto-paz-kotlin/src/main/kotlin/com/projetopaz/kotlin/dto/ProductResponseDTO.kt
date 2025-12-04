package com.projetopaz.kotlin.dto

data class ProductResponseDTO(
    val id: Long?,
    val name: String,
    val costPrice: Double,
    val salePrice: Double,
    val description: String?,
    val isFavorite: Boolean,
    val donation: Boolean,
    val supplier: SupplierResponseDTO?,
    val stock: StockDTO?,
    val categories: List<CategoryResponseDTO>,
    val images: List<ImageProductDTO>
)
