package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.CommunityResponseDTO
import com.projetopaz.kotlin.dto.ImageSaleResponseDTO
import com.projetopaz.kotlin.dto.ItemOrderDTO
import com.projetopaz.kotlin.dto.OrderResponseDTO
import com.projetopaz.kotlin.dto.SaleDTO
import com.projetopaz.kotlin.dto.SaleResponseDTO
import com.projetopaz.kotlin.model.Sale

object SaleMapper {

    fun toResponseDTO(entity: Sale): SaleResponseDTO {
        return SaleResponseDTO(
            id = entity.id,
            sellerId = entity.sellerId,
            isSelfService = entity.isSelfService,
            observation = entity.observation,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,

            community = entity.community?.let {
                CommunityResponseDTO(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    cep = it.cep,
                    quarter = it.quarter,
                    number = it.number,
                    complement = it.complement,
                    status = it.status,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            },

            images = entity.images.map { img ->
                ImageSaleResponseDTO(
                    id = img.id,
                    url = img.url,
                    alt = img.alt
                )
            },

            orders = entity.orders.map { order ->
                OrderResponseDTO(
                    id = order.id,
                    total = order.total_amount_order ?: 0.0,
                    status = if (order.status) 1 else 0,
                    items = order.items.map { item ->
                        ItemOrderDTO(
                            productId = item.productId,
                            quantity = item.quantity,
                            unitPrice = item.unitPrice
                        )
                    }
                )
            }
        )
    }
}

