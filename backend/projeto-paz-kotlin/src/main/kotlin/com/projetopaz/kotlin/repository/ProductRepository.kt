package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.active = true")
    fun findActive(): List<Product>
}