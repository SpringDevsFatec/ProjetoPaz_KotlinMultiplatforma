package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageBatchDTO
import com.projetopaz.kotlin.dto.ImageSaleResponseDTO
import com.projetopaz.kotlin.model.ImageSale
import com.projetopaz.kotlin.repository.ImageSaleRepository
import com.projetopaz.kotlin.repository.SaleRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional // Importante para banco de dados

@Service
class ImageSaleService(
    private val imageSaleRepository: ImageSaleRepository,
    private val saleRepository: SaleRepository,
    private val uploader: S3ImageUploader
) {

    @Transactional
    fun uploadImages(idSale: Long, batch: ImageBatchDTO): List<ImageSaleResponseDTO> {
        val sale = saleRepository.findById(idSale).orElseThrow {
            RuntimeException("Venda não encontrada")
        }

        val uploaded = mutableListOf<ImageSaleResponseDTO>()

        // CORREÇÃO: Agora percorremos a lista 'images'
        batch.images.forEach { dto ->

            // 1. Garante prefixo do Base64
            val base64 = if (dto.base64.startsWith("data:")) {
                dto.base64
            } else {
                "data:image/jpeg;base64,${dto.base64}"
            }

            // 2. Upload S3
            val url = uploader.uploadBase64(base64)

            // 3. Salva no Banco
            val image = ImageSale(
                url = url,
                alt = dto.alt ?: "Imagem da Venda $idSale",
                sale = sale,
                status = true
            )
            val saved = imageSaleRepository.save(image)

            // 4. Adiciona na resposta
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