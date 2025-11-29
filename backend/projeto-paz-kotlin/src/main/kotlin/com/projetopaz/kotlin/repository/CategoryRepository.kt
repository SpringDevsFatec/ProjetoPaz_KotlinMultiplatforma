package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    // Adicionado para buscar apenas as ativas
    fun findAllByActiveTrue(): List<Category>
}