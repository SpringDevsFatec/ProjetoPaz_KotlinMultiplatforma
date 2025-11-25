package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.CategoryDTO
import com.projetopaz.kotlin.dto.CategoryResponseDTO
import com.projetopaz.kotlin.model.Category
import java.time.LocalDateTime

object CategoryMapper {

    fun toEntity(dto: CategoryDTO, userId: Long?): Category {
        return Category(
            name = dto.name,
            categoryType = dto.categoryType,
            altImage = dto.altImage,
            favorite = dto.favorite,
            status = 1,
            createUser = userId,
            createdAt = LocalDateTime.now()
        )
    }

    fun toResponse(entity: Category): CategoryResponseDTO =
        CategoryResponseDTO(
            id = entity.id,
            name = entity.name,
            categoryType = entity.categoryType,
            imgCategory = entity.imgCategory,
            altImage = entity.altImage,
            favorite = entity.favorite,
            status = entity.status
        )
}
