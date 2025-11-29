package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.model.Category
import com.projetopaz.kotlin.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun findAll(): List<Category> {
        return categoryRepository.findAllByActiveTrue()
    }

    fun findById(id: Long): Category? {
        // Só retorna se estiver ativa
        return categoryRepository.findById(id)
            .filter { it.active }
            .orElse(null)
    }

    fun save(category: Category): Category {
        return categoryRepository.save(category)
    }

    // CORREÇÃO: Faz Soft Delete (Desativa em vez de apagar)
    fun delete(id: Long) {
        val category = categoryRepository.findById(id).orElse(null)
        if (category != null) {
            category.active = false
            categoryRepository.save(category)
        }
    }
}