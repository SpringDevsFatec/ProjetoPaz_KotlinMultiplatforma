package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.entity.Category
import com.projetopaz.kotlin.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    fun findById(id: Long): Optional<Category?> {
        return categoryRepository.findById(id)
    }

    fun findAllByIds(ids: List<Long>): List<Category> {
        if (ids.isEmpty()) return emptyList()
        return categoryRepository.findAllById(ids)
    }

    fun save(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun update(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun delete(id: Long) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id)
        }
    }
}