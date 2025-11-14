package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findAllBySaleIdAndStatusTrue(saleId: Long): List<Order>
}
