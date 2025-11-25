package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long>
