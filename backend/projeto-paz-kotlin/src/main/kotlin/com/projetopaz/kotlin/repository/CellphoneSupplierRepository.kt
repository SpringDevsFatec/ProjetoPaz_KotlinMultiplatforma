package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.CellphoneSupplier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CellphoneSupplierRepository : JpaRepository<CellphoneSupplier, Long>
