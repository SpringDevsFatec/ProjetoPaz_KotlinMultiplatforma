package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.SupplierAddress
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierAddressRepositoryDB : JpaRepository<SupplierAddress, Long> {

}