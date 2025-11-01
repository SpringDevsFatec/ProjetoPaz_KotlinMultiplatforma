package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByActiveTrue(): List<Category>
}