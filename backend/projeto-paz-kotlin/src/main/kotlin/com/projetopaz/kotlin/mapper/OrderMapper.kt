package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.OrderDTO
import com.projetopaz.kotlin.model.Order

object OrderMapper {
    fun toEntity(dto: OrderDTO) = Order(
        paymentMethod = dto.paymentMethod,
        total_amount_order = dto.total_amount_order, // ✅ Adicionado
        status = true
    )

    fun toDTO(entity: Order) = OrderDTO(
        paymentMethod = entity.paymentMethod,
        total_amount_order = entity.total_amount_order, // ✅ Adicionado
        items = entity.items.map { ItemOrderMapper.toDTO(it) }
    )
}
