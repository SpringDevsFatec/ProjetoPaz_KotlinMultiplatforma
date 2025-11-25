package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.ImageProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageProductRepository : JpaRepository<ImageProduct, Long>
