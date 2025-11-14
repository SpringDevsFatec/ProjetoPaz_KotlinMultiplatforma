package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.ProductImageDTO
import com.projetopaz.kotlin.entity.ProductImage
import org.springframework.stereotype.Component

@Component
class ProductImageMapper {

    fun toDTO(productImage: ProductImage): ProductImageDTO {
        return ProductImageDTO(
            id = productImage.id,
            url = productImage.url,
            altText = productImage.altText,
            isFavorite = productImage.isFavorite,
            product = productImage.product.id?:0
        )
    }
}