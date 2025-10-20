package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.SupplierCellphone
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierCellphoneRepositoryDB: JpaRepository<SupplierCellphone, Long> {

}