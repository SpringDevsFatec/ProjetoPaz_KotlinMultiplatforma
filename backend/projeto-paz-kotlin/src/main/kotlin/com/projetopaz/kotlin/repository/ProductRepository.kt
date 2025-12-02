package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Product
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByStatus(status: Int = 1): List<Product>
    fun findAllByIsFavoriteAndStatus(isFavorite: Boolean, status: Int = 1): List<Product>
    fun findAllByDonationAndStatus(donation: Boolean, status: Int = 1): List<Product>

    @EntityGraph(attributePaths = ["images", "categories", "stock", "supplier"])
    fun findByIdAndStatus(id: Long, status: Int = 1): Product?

    @EntityGraph(attributePaths = ["images", "categories", "stock", "supplier"])
    fun findAllByCategories_IdAndStatus(categoryId: Long, status: Int = 1): List<Product>

    @EntityGraph(attributePaths = ["images", "categories", "stock", "supplier"])
    fun findAllByNameContainingIgnoreCaseAndStatus(q: String, status: Int = 1): List<Product>
}
