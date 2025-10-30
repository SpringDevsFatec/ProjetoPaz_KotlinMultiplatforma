package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ProductImageDTO
import com.projetopaz.kotlin.mapper.ProductImageMapper
import com.projetopaz.kotlin.repository.ProductImageRepository

class ProductImageService(
    private val repository: ProductImageRepository,
    private val mapper: ProductImageMapper
) {

    fun findById(id: Long): ProductImageDTO {
        val productImage = repository.findById(id).orElseThrow {
            IllegalStateException("Produto não encontrado: $id")
        }
        return mapper.toDTO(productImage)
    }
}