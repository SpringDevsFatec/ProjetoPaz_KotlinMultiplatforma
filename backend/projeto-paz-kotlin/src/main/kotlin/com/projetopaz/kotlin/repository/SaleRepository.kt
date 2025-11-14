package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Sale
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

@Repository
interface SaleRepository : JpaRepository<Sale, Long> {

    @EntityGraph(
        attributePaths = ["community", "images", "orders", "orders.items"],
        type = EntityGraph.EntityGraphType.LOAD
    )
    fun findFullById(id: Long): Sale?

    @EntityGraph(
        attributePaths = ["community", "images", "orders", "orders.items"],
        type = EntityGraph.EntityGraphType.LOAD
    )
    fun findAllByStatus(status: Int): List<Sale>

    @EntityGraph(
        attributePaths = ["community", "images", "orders", "orders.items"],
        type = EntityGraph.EntityGraphType.LOAD
    )
    fun findAllBySellerId(sellerId: Long): List<Sale>

    @EntityGraph(
        attributePaths = ["community", "images", "orders", "orders.items"],
        type = EntityGraph.EntityGraphType.LOAD
    )
    fun findAllByCreatedAtBetween(start: LocalDate, end: LocalDate): List<Sale>

    fun findBySellerIdAndStatus(sellerId: Long, status: Int): Sale?
}

