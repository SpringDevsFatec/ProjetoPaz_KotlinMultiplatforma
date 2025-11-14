package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.ProductDTO
import com.projetopaz.kotlin.dto.ProductDTOView
import com.projetopaz.kotlin.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/product")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductDTOView>> {
        return ResponseEntity.ok(productService.findAll())
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok(productService.findById(id))
    }

    @PostMapping
    fun create(@RequestBody @Valid dto: ProductDTO): ResponseEntity<ProductDTO> {
        val saved = productService.save(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
    /*
    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @Valid @RequestBody productDetails: Product): ResponseEntity<Product> {
        val updatedProduct = productService.update(id, productDetails)
        return ResponseEntity.ok(updatedProduct)
    }
    */

    /*
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }

     */
}