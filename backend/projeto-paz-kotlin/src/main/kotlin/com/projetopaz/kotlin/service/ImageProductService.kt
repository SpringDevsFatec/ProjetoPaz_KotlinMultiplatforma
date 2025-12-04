package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageBatchDTO
import com.projetopaz.kotlin.model.ImageProduct
import com.projetopaz.kotlin.repository.ImageProductRepository
import com.projetopaz.kotlin.repository.ProductRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImageProductService(
    private val imageRepository: ImageProductRepository,
    private val productRepository: ProductRepository,
    private val s3Uploader: S3ImageUploader
) {

    @Transactional
    fun uploadImages(productId: Long, batch: ImageBatchDTO): Boolean {
        val product = productRepository.findById(productId).orElse(null) ?: return false

        return try {
            // Percorre a lista de imagens enviadas
            batch.images.forEach { imgDto ->
                // 1. Sobe pro S3 (Ajustando prefixo se necessario)
                val base64 = if (imgDto.base64.startsWith("data:")) imgDto.base64 else "data:image/jpeg;base64,${imgDto.base64}"
                val url = s3Uploader.uploadBase64(base64)

                // 2. Salva no Banco vinculada ao produto
                val imageEntity = ImageProduct(
                    url = url,
                    product = product,
                    status = 1
                )
                imageRepository.save(imageEntity)
            }
            true
        } catch (e: Exception) {
            println("Erro ao salvar imagens: ${e.message}")
            false
        }
    }

    fun deleteImage(id: Long): Boolean {
        val img = imageRepository.findById(id).orElse(null) ?: return false
        img.status = 0
        imageRepository.save(img)
        return true
    }
}