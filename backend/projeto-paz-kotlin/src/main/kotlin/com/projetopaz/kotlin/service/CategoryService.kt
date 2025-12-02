package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.CategoryDTO
import com.projetopaz.kotlin.model.Category
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val s3Uploader: S3ImageUploader
) {

    fun create(dto: CategoryDTO, userId: Long): Category {

        var urlDaImagem: String? = null

        // LÓGICA DE UPLOAD
        if (!dto.imgBase64.isNullOrBlank()) {
            try {
                // O seu S3ImageUploader exige o prefixo "data:image...", mas o App manda puro.
                // Vamos adicionar o prefixo manualmente se ele não existir para não dar erro.
                val base64Pronto = if (dto.imgBase64.startsWith("data:")) {
                    dto.imgBase64
                } else {
                    "data:image/jpeg;base64,${dto.imgBase64}"
                }

                urlDaImagem = s3Uploader.uploadBase64(base64Pronto)

            } catch (e: Exception) {
                println("Erro no upload da imagem: ${e.message}")
                // Continua o cadastro mesmo se a imagem falhar (opcional)
            }
        }

        val category = Category(
            name = dto.name,
            categoryType = dto.categoryType,
            // description = dto.description, // Descomente se já tiver o campo na Entity
            favorite = dto.favorite,
            imgCategory = urlDaImagem ?: dto.altImage, // Salva a URL do S3
            createUser = userId,
            createdAt = LocalDateTime.now(),
            status = 1
        )

        return repository.save(category)
    }

    fun findAll(): List<Category> {
        return repository.findByStatus(1)
    }

    fun findById(id: Long): Category? = repository.findById(id).orElse(null)

    fun updateFields(id: Long, dto: CategoryDTO, userId: Long): Category? {
        val existing = repository.findById(id).orElse(null) ?: return null

        existing.name = dto.name
        existing.categoryType = dto.categoryType
        existing.favorite = dto.favorite
        existing.description = dto.description

        // Se mandou imagem nova, faz upload e atualiza
        if (!dto.imgBase64.isNullOrBlank()) {
            try {
                val base64Pronto = if (dto.imgBase64.startsWith("data:")) {
                    dto.imgBase64
                } else {
                    "data:image/jpeg;base64,${dto.imgBase64}"
                }
                existing.imgCategory = s3Uploader.uploadBase64(base64Pronto)
            } catch (e: Exception) {
                println("Erro ao atualizar imagem: ${e.message}")
            }
        }

        existing.updatedAt = LocalDateTime.now()
        existing.updateUser = userId

        return repository.save(existing)
    }

    fun delete(id: Long, userId: Long): Boolean {
        val existing = repository.findById(id).orElse(null) ?: return false
        existing.status = 0
        existing.updatedAt = LocalDateTime.now()
        existing.updateUser = userId
        repository.save(existing)
        return true
    }

    // Método legado (pode deixar retornando null ou remover se não usado)
    fun updateImage(id: Long, dto: Any?, userId: Long): Category? {
        return null
    }
}