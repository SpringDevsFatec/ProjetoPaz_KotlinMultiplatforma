package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageBatchDTO
import com.projetopaz.kotlin.dto.ImageSaleDTO
import com.projetopaz.kotlin.dto.ImageSaleResponseDTO
import com.projetopaz.kotlin.mapper.ImageSaleMapper
import com.projetopaz.kotlin.model.ImageSale
import com.projetopaz.kotlin.repository.ImageSaleRepository
import com.projetopaz.kotlin.repository.SaleRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service

@Service
class ImageSaleService(
    private val imageSaleRepository: ImageSaleRepository,
    private val saleRepository: SaleRepository,
    private val uploader: S3ImageUploader
) {
    fun uploadImages(idSale: Long, batch: ImageBatchDTO): List<ImageSaleResponseDTO> {
        val sale = saleRepository.findById(idSale).orElseThrow {
            RuntimeException("Venda não encontrada")
        }

        val uploaded = mutableListOf<ImageSaleResponseDTO>()

        batch.imgs.values.forEach { dto ->
            val url = uploader.uploadBase64(dto.base64)
            val image = ImageSale(
                url = url,
                alt = dto.alt.toString(),
                sale = sale
            )
            val saved = imageSaleRepository.save(image)
            uploaded.add(
                ImageSaleResponseDTO(
                    id = saved.id,
                    url = saved.url,
                    alt = saved.alt
                )
            )
        }

        return uploaded
    }



    fun deleteImage(id: Long): Boolean {
        val image = imageSaleRepository.findById(id).orElse(null) ?: return false
        image.status = false
        imageSaleRepository.save(image)
        return true
    }
}
