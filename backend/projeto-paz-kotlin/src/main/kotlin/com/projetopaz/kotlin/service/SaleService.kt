package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.SaleDTO
import com.projetopaz.kotlin.model.Sale
import com.projetopaz.kotlin.repository.CommunityRepository
import com.projetopaz.kotlin.repository.SaleRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SaleService(
    private val saleRepository: SaleRepository,
    private val communityRepository: CommunityRepository
)
 {

    fun create(dto: SaleDTO): Sale {
        val sellerId = dto.sellerId ?: throw IllegalArgumentException("Seller ID não pode ser nulo.")

        val existing = saleRepository.findBySellerIdAndStatus(sellerId, 1)
        if (existing != null) return existing

        val community = dto.communityId?.let {
            communityRepository.findById(it).orElse(null)
                ?: throw IllegalArgumentException("Community com id=${dto.communityId} não existe.")
        }

        val sale = Sale(
            sellerId = sellerId,
            isSelfService = dto.isSelfService,
            observation = dto.observation,
            status = dto.status,
            community = community,
            createdAt = LocalDate.now(),
            updatedAt = LocalDate.now()
        )

        return saleRepository.save(sale)
    }


     /**
     * GET by ID carregando relacionamentos via EntityGraph
     */
    fun findFull(id: Long): Sale? =
        saleRepository.findFullById(id)

    /**
     * GET all (referência simples, sem EntityGraph)
     */
    fun findAll(): List<Sale> =
        saleRepository.findAll()

    /**
     * GET por status
     */
    fun findAllstatus(status: Int): List<Sale> =
        saleRepository.findAllByStatus(status)

    /**
     * GET por vendedor
     */
    fun findBySeller(id: Long): List<Sale> =
        saleRepository.findAllBySellerId(id)

    /**
     * GET por intervalo de datas
     */
    fun findByPeriod(start: LocalDate, end: LocalDate): List<Sale> {
        if (end.isBefore(start)) {
            throw IllegalArgumentException("A data final não pode ser menor que a inicial.")
        }

        return saleRepository.findAllByCreatedAtBetween(start, end)
    }

    /**
     * COMPLETE SALE
     */
    fun completeSale(id: Long, observation: String): Sale? {
        val sale = saleRepository.findById(id).orElse(null)
            ?: return null

        sale.status = 2
        sale.observation = observation
        sale.updatedAt = LocalDate.now()

        return saleRepository.save(sale)
    }

    /**
     * CANCEL SALE
     */
    fun cancelSale(id: Long): Boolean {
        val sale = saleRepository.findById(id).orElse(null)
            ?: return false

        sale.status = 0
        sale.updatedAt = LocalDate.now()

        saleRepository.save(sale)
        return true
    }
}
