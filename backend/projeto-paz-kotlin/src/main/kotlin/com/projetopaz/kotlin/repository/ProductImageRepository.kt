package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageRepository: JpaRepository<ProductImage, Long> {
}