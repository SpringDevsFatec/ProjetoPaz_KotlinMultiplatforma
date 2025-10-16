package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.projetopaz.kotlin.dto.ProductRequest

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

    @GetMapping("/search")
    fun searchProducts(@RequestParam("q") query: String): ResponseEntity<List<Product>> {
        val products = productService.searchByName(query)
        return ResponseEntity.ok(products)
    }

    @PostMapping
    fun createProduct(@Valid @RequestBody productRequest: ProductRequest): ResponseEntity<Product> {
        val savedProduct = productService.save(productRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @Valid @RequestBody productRequest: ProductRequest): ResponseEntity<Product> {
        val updatedProduct = productService.update(id, productRequest)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}