package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.Supplier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SupplierRepository : JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE s.active = true")
    fun findActive(): List<Supplier>
}