package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.ProductDTO
import com.projetopaz.kotlin.dto.ProductDTOView
import com.projetopaz.kotlin.dto.ProductImageDTO
import com.projetopaz.kotlin.dto.StockDTO
import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.entity.Stock
import com.projetopaz.kotlin.service.CategoryService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ProductMapper(
    val categoryService: CategoryService
) {

    fun fromDTO(dto: ProductDTO): Product {

        //val categories = dto.categories.mapNotNull { categoryService.findById(it) }.toMutableSet()

        val stock = Stock(
            quantity = dto.stock?.quantity ?: 0,
            fabrication = dto.stock?.fabrication ?: LocalDate.now(),
            maturity = dto.stock?.maturity ?: LocalDate.now(),
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )

        return Product(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            costPrice = dto.costPrice,
            salePrice = dto.salePrice,
            isFavorite = dto.isFavorite,
            isDonation = dto.isDonation,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            //categories = categories,
            supplier = null,
            stock = stock
        )
    }
    fun toDTO(product: Product): ProductDTO {
        return ProductDTO(
            id = product.id,
            name = product.name,
            description = product.description,
            costPrice = product.costPrice,
            salePrice = product.salePrice,
            isFavorite = product.isFavorite,
            isDonation = product.isDonation,
            //categories = product.categories.mapNotNull { it.id },
            supplier = product.supplier?.id,
            images = product.images.map {
                ProductImageDTO(it.id, it.url, it.altText, it.isFavorite, it.product.id)
            },
            stock = StockDTO(
                quantity = product.stock?.quantity ?: 0,
                fabrication = product.stock?.fabrication ?: LocalDate.now(),
                maturity = product.stock?.maturity ?: LocalDate.now(),
            )
        )
    }
    fun toDTOView(product: Product): ProductDTOView {
        println("Mapper chamado para produto ${product.id}")
        return ProductDTOView(
            id = product.id,
            name = product.name,
            salePrice = product.salePrice,
            //categories = product.categories
                //.filter { it.active && !it.name.isNullOrBlank() }
                //.mapNotNull { it.name },
            favoriteImageUrl = product.images.firstOrNull { it.isFavorite }?.url
        )
    }
}