package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageUploadDTO
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service

@Service
class CategoryImageService(
    private val repository: CategoryRepository,
    private val uploader: S3ImageUploader
) {

    fun uploadCategoryImage(id: Long, dto: ImageUploadDTO): String? {

        val category = repository.findById(id).orElse(null) ?: return null

        val url = uploader.uploadBase64(dto.base64)
        category.imgCategory = url

        repository.save(category)

        return url
    }
}
