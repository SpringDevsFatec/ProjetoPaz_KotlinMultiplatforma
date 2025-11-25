package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageBatchDTO
import com.projetopaz.kotlin.dto.ImageProductDTO
import com.projetopaz.kotlin.model.ImageProduct
import com.projetopaz.kotlin.repository.ImageProductRepository
import com.projetopaz.kotlin.repository.ProductRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service

@Service
class ImageProductService(
    private val imageRepo: ImageProductRepository,
    private val productRepo: ProductRepository,
    private val uploader: S3ImageUploader
) {

    fun uploadImages(productId: Long, batch: ImageBatchDTO): List<ImageProductDTO> {
        val product = productRepo.findById(productId).orElseThrow { RuntimeException("Product not found") }
        val uploaded = mutableListOf<ImageProductDTO>()

        batch.imgs.values.forEach { item ->
            val url = uploader.uploadBase64(item.base64)
            val image = ImageProduct(url = url, alt = item.alt, product = product)
            val saved = imageRepo.save(image)
            product.images.add(saved)
            uploaded.add(ImageProductDTO(saved.id, saved.url, saved.alt))
        }
        productRepo.save(product) // persist relation
        return uploaded
    }

    fun deleteImage(id: Long): Boolean {
        val img = imageRepo.findById(id).orElse(null) ?: return false
        img.status = 0
        imageRepo.save(img)
        return true
    }
}
