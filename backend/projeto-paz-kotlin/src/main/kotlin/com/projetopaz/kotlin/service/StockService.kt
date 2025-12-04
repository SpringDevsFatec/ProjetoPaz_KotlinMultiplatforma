package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.StockDTO
import com.projetopaz.kotlin.model.Stock
import com.projetopaz.kotlin.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockService(private val stockRepository: StockRepository) {

    fun createOrUpdate(dto: StockDTO?): Stock? {
        if (dto == null) return null

        val stock = dto.id?.let { stockRepository.findById(it).orElse(null) } ?: Stock()
        stock.name = dto.name
        stock.qtd = dto.qtd
        stock.maturity = dto.maturity
        stock.fabrication = dto.fabrication
        stock.status = 1
        return stockRepository.save(stock)
    }
}
