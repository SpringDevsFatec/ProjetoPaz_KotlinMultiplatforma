package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ProductRequest
import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.repository.ProductRepository
import com.projetopaz.kotlin.repository.SupplierRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val supplierRepository: SupplierRepository
) {

    fun findAll(): List<Product> {
        return productRepository.findAllByActiveTrue()
    }

    fun findById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID $id não encontrado") }
    }

    fun searchByName(name: String): List<Product> {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
    }

    @Transactional
    fun save(request: ProductRequest): Product {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID ${request.categoryId} não encontrada") }

        val supplier = request.supplierId?.let {
            supplierRepository.findById(it)
                .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor com ID $it não encontrado") }
        }

        val newProduct = Product(
            name = request.name,
            description = request.description,
            sku = request.sku,
            price = BigDecimal.valueOf(request.price),
            active = request.active,
            category = category,
            supplier = supplier
        )
        return productRepository.save(newProduct)
    }

    @Transactional
    fun update(id: Long, request: ProductRequest): Product {
        val existingProduct = findById(id)

        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID ${request.categoryId} não encontrada") }

        val supplier = request.supplierId?.let {
            supplierRepository.findById(it)
                .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor com ID $it não encontrado") }
        }

        val updatedProduct = existingProduct.copy(
            name = request.name,
            description = request.description,
            sku = request.sku,
            price = BigDecimal.valueOf(request.price),
            active = request.active,
            category = category,
            supplier = supplier,
            updatedAt = LocalDateTime.now()
        )
        return productRepository.save(updatedProduct)
    }

    @Transactional
    fun delete(id: Long) {
        val product = findById(id)
        product.active = false
        productRepository.save(product)
    }
}