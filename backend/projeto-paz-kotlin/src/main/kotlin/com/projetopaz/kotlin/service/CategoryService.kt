package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.model.Category
import com.projetopaz.kotlin.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    fun findById(id: Long): Category? {
        return categoryRepository.findById(id).orElse(null)
    }

    fun save(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun delete(id: Long) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id)
        }
    }
}