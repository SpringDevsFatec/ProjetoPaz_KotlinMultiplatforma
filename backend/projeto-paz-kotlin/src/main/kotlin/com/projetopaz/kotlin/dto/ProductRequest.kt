package com.projetopaz.kotlin.dto

data class ProductRequest(
    val name: String,
    val description: String?,
    val sku: String,
    val price: Double,
    val active: Boolean = true,
    val categoryId: Long,
    val supplierId: Long?
)