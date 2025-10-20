package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.model.Product
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    fun findAll(): List<Product> {
        return productRepository.findAllByActiveTrue()
    }

    fun findById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID $id não encontrado") }
    }

    fun save(product: Product): Product {
        val categoryId = product.category.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto precisa ter um ID de categoria.")

        val existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID $categoryId não existe.") }

        product.category = existingCategory
        return productRepository.save(product)
    }

    fun update(id: Long, productDetails: Product): Product {
        val existingProduct = findById(id)

        val categoryId = productDetails.category.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto precisa ter um ID de categoria.")

        val existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID $categoryId não existe.") }

        val updatedProduct = existingProduct.copy(
            name = productDetails.name,
            description = productDetails.description,
            sku = productDetails.sku,
            price = productDetails.price,
            active = productDetails.active,
            category = existingCategory,
            updatedAt = LocalDateTime.now()
        )
        return productRepository.save(updatedProduct)
    }

    fun delete(id: Long) {
        val product = findById(id)
        product.active = false
        productRepository.save(product)
    }
}