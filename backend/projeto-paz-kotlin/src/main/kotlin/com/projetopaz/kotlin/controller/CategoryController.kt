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

    // CREATE com imagem (Base64 → S3 → URL salvo)
    @PostMapping
    fun create(
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: CategoryDTO
    ): ResponseEntity<Any> {

        val userId = jwtUtil.extractUserId(token)
        val result = service.create(dto, userId)

        return ResponseEntity.ok(CategoryMapper.toResponse(result))
    }

    // UPDATE SOMENTE DOS CAMPOS
    @PutMapping("/{id}")
    fun updateFields(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: CategoryDTO
    ): ResponseEntity<Any> {

        val userId = jwtUtil.extractUserId(token)
        val updated = service.updateFields(id, dto, userId)

        return updated?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    // UPDATE SOMENTE DA IMAGEM
    @PutMapping("/{id}/image")
    fun updateImage(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: ImageUploadDTO
    ): ResponseEntity<Any> {

        val userId = jwtUtil.extractUserId(token)
        val updated = service.updateImage(id, dto, userId)

        return updated?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Any> {

        val userId = jwtUtil.extractUserId(token)
        val deleted = service.delete(id, userId)

        return if (deleted) ResponseEntity.ok("Categoria inativada com sucesso.")
        else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<Any> =
        ResponseEntity.ok(service.findAll().map { CategoryMapper.toResponse(it) })

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Any> {
        val category = service.findById(id)
        return category?.let { ResponseEntity.ok(CategoryMapper.toResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }
}
