package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.ImageSaleDTO
import com.projetopaz.kotlin.model.ImageSale
object ImageSaleMapper {
    fun toEntity(dto: ImageSaleDTO) = ImageSale(
        url = dto.url ?: "",
        alt = dto.alt,
        status = dto.status
    )

    fun toDTO(entity: ImageSale) = ImageSaleDTO(
        id = entity.id,
        url = entity.url,
        alt = entity.alt,
        status = entity.status
    )
}


