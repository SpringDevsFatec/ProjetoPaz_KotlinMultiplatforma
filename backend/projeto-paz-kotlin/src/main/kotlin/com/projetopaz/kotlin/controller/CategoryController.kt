package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.CategoryDTO
import com.projetopaz.kotlin.dto.ImageUploadDTO
import com.projetopaz.kotlin.mapper.CategoryMapper
import com.projetopaz.kotlin.security.TokenService
import com.projetopaz.kotlin.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val service: CategoryService,
    private val jwtUtil: TokenService
) {

    // CREATE (Com correção do ID)
    @PostMapping
    fun create(
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: CategoryDTO
    ): ResponseEntity<Any> {
        // O "?: 0L" garante que se o token não tiver ID, usa 0 (evita o erro TypeMismatch)
        val userId = jwtUtil.extractUserId(token) ?: 0L
        val result = service.create(dto, userId)
        return ResponseEntity.ok(CategoryMapper.toResponse(result))
    }

    // UPDATE FIELDS
    @PutMapping("/{id}")
    fun updateFields(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: CategoryDTO
    ): ResponseEntity<Any> {
        val userId = jwtUtil.extractUserId(token) ?: 0L
        val updated = service.updateFields(id, dto, userId)
        return updated?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    // DELETE
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Any> {
        val userId = jwtUtil.extractUserId(token) ?: 0L
        val deleted = service.delete(id, userId)
        return if (deleted) ResponseEntity.ok("Categoria inativada com sucesso.")
        else ResponseEntity.notFound().build()
    }

    // MÉTODOS PÚBLICOS (GET)
    @GetMapping
    fun findAll(): ResponseEntity<Any> =
        ResponseEntity.ok(service.findAll().map { CategoryMapper.toResponse(it) })

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Any> {
        val category = service.findById(id)
        return category?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    // UPDATE IMAGE (Se ainda usar este endpoint específico)
    @PutMapping("/{id}/image")
    fun updateImage(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: ImageUploadDTO
    ): ResponseEntity<Any> {
        val userId = jwtUtil.extractUserId(token) ?: 0L
        val updated = service.updateImage(id, dto, userId)
        return updated?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }
}