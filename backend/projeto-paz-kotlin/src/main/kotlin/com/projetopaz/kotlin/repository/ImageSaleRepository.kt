package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.ImageSale
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageSaleRepository : JpaRepository<ImageSale, Long> {
    fun findAllBySaleIdAndStatusTrue(saleId: Long): List<ImageSale>
}
