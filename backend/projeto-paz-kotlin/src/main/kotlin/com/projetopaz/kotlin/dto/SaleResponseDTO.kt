package com.projetopaz.kotlin.dto

import java.time.LocalDate

data class SaleResponseDTO(
    val id: Long,
    val sellerId: Long,
    val isSelfService: Boolean,
    val observation: String?,
    val status: Int,
    val createdAt: LocalDate?,
    val updatedAt: LocalDate?,
    val community: CommunityResponseDTO?,
    val orders: List<OrderResponseDTO>,
    val images: List<ImageSaleResponseDTO>
)
