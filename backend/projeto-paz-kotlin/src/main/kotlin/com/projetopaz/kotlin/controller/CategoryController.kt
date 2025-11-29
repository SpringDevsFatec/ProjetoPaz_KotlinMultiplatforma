package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.model.Category
import com.projetopaz.kotlin.service.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun createCategory(@Valid @RequestBody category: Category): ResponseEntity<Category> {
        // CORREÇÃO: Força o ID ser nulo para o JPA criar (INSERT) em vez de atualizar (UPDATE)
        val newCategory = category.copy(id = null)
        val savedCategory = categoryService.save(newCategory)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory)
    }

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<Category>> {
        val categories = categoryService.findAll()
        return ResponseEntity.ok(categories)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Category> {
        val category = categoryService.findById(id)
        return category?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @Valid @RequestBody categoryToUpdate: Category): ResponseEntity<Category> {
        return categoryService.findById(id)?.let {
            val updatedCategory = it.copy(
                name = categoryToUpdate.name,
                description = categoryToUpdate.description,
                active = categoryToUpdate.active
            )
            ResponseEntity.ok(categoryService.save(updatedCategory))
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.delete(id)
        return ResponseEntity.noContent().build()
    }
}