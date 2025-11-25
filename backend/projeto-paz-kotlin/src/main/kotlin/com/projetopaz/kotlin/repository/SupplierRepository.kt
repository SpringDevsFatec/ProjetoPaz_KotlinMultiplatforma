package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Supplier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupplierRepository : JpaRepository<Supplier, Long> {
    fun findAllByStatus(status: Int = 1): List<Supplier>
    fun findByIdAndStatus(id: Long, status: Int = 1): Supplier?
}
