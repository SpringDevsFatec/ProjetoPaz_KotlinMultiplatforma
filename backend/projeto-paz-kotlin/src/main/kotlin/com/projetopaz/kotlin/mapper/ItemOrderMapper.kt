package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.ItemOrderDTO
import com.projetopaz.kotlin.model.ItemOrder

object ItemOrderMapper {
    fun toEntity(dto: ItemOrderDTO) = ItemOrder(
        productId = dto.productId,
        quantity = dto.quantity,
        unitPrice = dto.unitPrice
    )

    fun toDTO(entity: ItemOrder) = ItemOrderDTO(
        productId = entity.productId,
        quantity = entity.quantity,
        unitPrice = entity.unitPrice
    )
}
