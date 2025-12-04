package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.SupplierAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupplierAddressRepository : JpaRepository<SupplierAddress, Long>
