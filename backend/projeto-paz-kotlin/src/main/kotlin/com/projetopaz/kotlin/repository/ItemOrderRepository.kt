package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.ItemOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemOrderRepository : JpaRepository<ItemOrder, Long> {
    fun findAllByOrderIdAndStatusTrue(orderId: Long): List<ItemOrder>
}
