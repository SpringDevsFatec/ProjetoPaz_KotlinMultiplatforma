package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ProductCreateDTO
import com.projetopaz.kotlin.mapper.ProductMapper
import com.projetopaz.kotlin.model.Product
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.repository.ProductRepository
import com.projetopaz.kotlin.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val supplierRepository: SupplierRepository,
    private val stockService: StockService
) {

    @Transactional
    fun create(dto: ProductCreateDTO, userId: Long?): Product {
        val product = Product(
            name = dto.name,
            costPrice = dto.costPrice,
            salePrice = dto.salePrice,
            description = dto.description,
            isFavorite = dto.isFavorite,
            donation = dto.donation,
            createUser = userId
        )

        // supplier
        dto.supplierId?.let { sid ->
            val supplier = supplierRepository.findById(sid).orElse(null)
            product.supplier = supplier
        }

        // stock
        product.stock = stockService.createOrUpdate(dto.stock)

        // categories
        dto.categoryIds.forEach { cid ->
            categoryRepository.findById(cid).ifPresent { c -> product.categories.add(c) }
        }

        return productRepository.save(product)
    }

    @Transactional
    fun update(id: Long, dto: ProductCreateDTO, userId: Long?): Product? {
        val existing = productRepository.findById(id).orElse(null) ?: return null

        existing.name = dto.name
        existing.costPrice = dto.costPrice
        existing.salePrice = dto.salePrice
        existing.description = dto.description
        existing.isFavorite = dto.isFavorite
        existing.donation = dto.donation
        existing.updatedAt = LocalDateTime.now()
        existing.updateUser = userId

        // supplier
        dto.supplierId?.let { sid -> existing.supplier = supplierRepository.findById(sid).orElse(null) }

        // stock
        existing.stock = stockService.createOrUpdate(dto.stock)

        // categories (replace)
        existing.categories.clear()
        dto.categoryIds.forEach { cid -> categoryRepository.findById(cid).ifPresent { c -> existing.categories.add(c) } }

        return productRepository.save(existing)
    }

    fun delete(id: Long, userId: Long?): Boolean {
        val product = productRepository.findById(id).orElse(null) ?: return false

        product.status = 0
        product.updatedAt = LocalDateTime.now()
        product.updateUser = userId
        productRepository.save(product)
        return true
    }

    fun findAll(): List<Product> = productRepository.findAllByStatus(1)

    fun findById(id: Long): Product? = productRepository.findByIdAndStatus(id, 1)

    fun findByCategory(categoryId: Long): List<Product> = productRepository.findAllByCategories_IdAndStatus(categoryId, 1)

    fun findFavorites(): List<Product> = productRepository.findAllByIsFavoriteAndStatus(true, 1)

    fun findDonations(): List<Product> = productRepository.findAllByDonationAndStatus(true, 1)

    fun searchByName(q: String): List<Product> = productRepository.findAllByNameContainingIgnoreCaseAndStatus(q, 1)
}
