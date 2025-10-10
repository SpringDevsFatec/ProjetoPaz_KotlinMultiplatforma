package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.service.ProductService
import jakarta.validation.Valid // Não se esqueça de importar
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<Product>> {
        val products = productService.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.findById(id)
        return ResponseEntity.ok(product)
    }

    @PostMapping
    fun createProduct(@Valid @RequestBody product: Product): ResponseEntity<Product> {
        val savedProduct = productService.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @Valid @RequestBody productDetails: Product): ResponseEntity<Product> {
        val updatedProduct = productService.update(id, productDetails)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}