package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.mapper.ProductMapper
import com.projetopaz.kotlin.service.ImageProductService
import com.projetopaz.kotlin.service.ProductService
import com.projetopaz.kotlin.security.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController(
    private val productService: ProductService,
    private val imageService: ImageProductService,
    private val tokenService: TokenService
) {

    @PostMapping
    fun create(
        @RequestHeader("Authorization") token: String?,
        @RequestBody dto: ProductCreateDTO
    ): ResponseEntity<Any> {
        val userId = token?.removePrefix("Bearer ")?.let { tokenService.extractUserId(it) }
        val created = productService.create(dto, userId)
        return ResponseEntity.ok(ProductMapper.toProductResponse(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String?,
        @RequestBody dto: ProductCreateDTO
    ): ResponseEntity<Any> {
        val userId = token?.removePrefix("Bearer ")?.let { tokenService.extractUserId(it) }
        val updated = productService.update(id, dto, userId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProductMapper.toProductResponse(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String?
    ): ResponseEntity<Any> {
        val userId = token?.removePrefix("Bearer ")?.let { tokenService.extractUserId(it) }
        val deleted = productService.delete(id, userId)
        return if (deleted) ResponseEntity.ok(mapOf("message" to "Product inativado")) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<ProductResponseDTO>> =
        ResponseEntity.ok(productService.findAll().map { ProductMapper.toProductResponse(it) })

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Any> {
        val p = productService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProductMapper.toProductResponse(p))
    }

    @GetMapping("/category/{categoryId}")
    fun byCategory(@PathVariable categoryId: Long) =
        ResponseEntity.ok(productService.findByCategory(categoryId).map { ProductMapper.toProductResponse(it) })

    @GetMapping("/favorite")
    fun favorites() = ResponseEntity.ok(productService.findFavorites().map { ProductMapper.toProductResponse(it) })

    @GetMapping("/donation")
    fun donations() = ResponseEntity.ok(productService.findDonations().map { ProductMapper.toProductResponse(it) })

    @GetMapping("/search")
    fun search(@RequestParam("q") q: String) =
        ResponseEntity.ok(productService.searchByName(q).map { ProductMapper.toProductResponse(it) })

    // images endpoints
    @PostMapping("/img/{productId}")
    fun uploadImages(@PathVariable productId: Long, @RequestBody batch: ImageBatchDTO): ResponseEntity<Any> {
        val uploaded = imageService.uploadImages(productId, batch)
        return ResponseEntity.ok(mapOf("uploaded" to uploaded))
    }

    @DeleteMapping("/img/{idImage}")
    fun deleteImage(@PathVariable idImage: Long): ResponseEntity<Any> =
        if (imageService.deleteImage(idImage)) ResponseEntity.ok(mapOf("message" to "Imagem inativada"))
        else ResponseEntity.notFound().build()
}
