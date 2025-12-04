package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.model.*

object ProductMapper {

    fun toStockEntity(dto: StockDTO?): Stock? {
        if (dto == null) return null
        return Stock(
            id = dto.id,
            name = dto.name,
            qtd = dto.qtd,
            maturity = dto.maturity,
            fabrication = dto.fabrication
        )
    }

    fun toSupplierResponse(entity: Supplier?): SupplierResponseDTO? {
        if (entity == null) return null

        return SupplierResponseDTO(
            id = entity.id,
            name = entity.name,
            cnpj = entity.cnpj,
            type = entity.type,
            occupation = entity.occupation,
            observation = entity.observation,
            status = entity.status
        )
    }

    fun toCategoryResponse(entity: Category): CategoryResponseDTO =
        CategoryResponseDTO(
            id = entity.id,
            name = entity.name,
            categoryType = entity.categoryType,
            imgCategory = entity.imgCategory,
            altImage = entity.altImage,
            favorite = entity.favorite,
            status = entity.status
        )

    fun toProductResponse(entity: Product): ProductResponseDTO =
        ProductResponseDTO(
            id = entity.id,
            name = entity.name,
            costPrice = entity.costPrice,
            salePrice = entity.salePrice,
            description = entity.description,
            isFavorite = entity.isFavorite,
            donation = entity.donation,

            supplier = toSupplierResponse(entity.supplier),

            stock = entity.stock?.let {
                StockDTO(
                    id = it.id,
                    name = it.name,
                    qtd = it.qtd,
                    maturity = it.maturity,
                    fabrication = it.fabrication
                )
            },

            categories = entity.categories.map { toCategoryResponse(it) },

            images = entity.images.map {
                ImageProductDTO(
                    id = it.id,
                    url = it.url,
                    alt = it.alt
                )
            }
        )
}
