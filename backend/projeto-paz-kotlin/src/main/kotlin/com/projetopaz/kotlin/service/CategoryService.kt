package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.CategoryDTO
import com.projetopaz.kotlin.dto.ImageUploadDTO
import com.projetopaz.kotlin.mapper.CategoryMapper
import com.projetopaz.kotlin.model.Category
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val uploader: S3ImageUploader
) {

    // CREATE com upload da imagem
    fun create(dto: CategoryDTO, userId: Long?): Category {
        val entity = CategoryMapper.toEntity(dto, userId)

        // Upload da imagem enviada no create
        dto.imgBase64?.let {
            val url = uploader.uploadBase64(it)
            entity.imgCategory = url
        }

        return repository.save(entity)
    }

    // UPDATE dos campos (sem imagem)
    fun updateFields(id: Long, dto: CategoryDTO, userId: Long?): Category? {
        val category = repository.findById(id).orElse(null) ?: return null

        category.name = dto.name
        category.categoryType = dto.categoryType
        category.altImage = dto.altImage
        category.favorite = dto.favorite
        category.updatedAt = LocalDateTime.now()
        category.updateUser = userId

        return repository.save(category)
    }

    // UPDATE somente da imagem
    fun updateImage(id: Long, dto: ImageUploadDTO, userId: Long?): Category? {
        val category = repository.findById(id).orElse(null) ?: return null

        val newUrl = uploader.uploadBase64(dto.base64)

        category.imgCategory = newUrl
        category.updatedAt = LocalDateTime.now()
        category.updateUser = userId

        return repository.save(category)
    }

    fun delete(id: Long, userId: Long?): Boolean {
        val category = repository.findById(id).orElse(null) ?: return false

        category.status = 0
        category.updatedAt = LocalDateTime.now()
        category.updateUser = userId

        repository.save(category)
        return true
    }

    fun findAll(): List<Category> =
        repository.findAllByStatus(1)

    fun findById(id: Long): Category? =
        repository.findById(id).orElse(null)
}
