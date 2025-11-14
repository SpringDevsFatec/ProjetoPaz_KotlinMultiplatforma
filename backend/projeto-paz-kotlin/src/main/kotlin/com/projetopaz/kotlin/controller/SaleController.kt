package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.ImageBatchDTO
import com.projetopaz.kotlin.dto.ImageSaleDTO
import com.projetopaz.kotlin.dto.SaleDTO
import com.projetopaz.kotlin.dto.SaleResponseDTO
import com.projetopaz.kotlin.mapper.SaleMapper
import com.projetopaz.kotlin.model.Sale
import com.projetopaz.kotlin.service.ImageSaleService
import com.projetopaz.kotlin.service.SaleService
import com.projetopaz.kotlin.security.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDate
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/api/sale")
class SaleController(
    private val saleService: SaleService,
    private val imageSaleService: ImageSaleService,
    private val tokenService: TokenService
) {

    /**
     * CREATE SALE
     */
    @PostMapping
    fun create(
        @RequestBody dto: SaleDTO,
        request: HttpServletRequest
    ): ResponseEntity<SaleResponseDTO> {

        val token = request.getHeader("Authorization")
            ?.removePrefix("Bearer ")
            ?.trim()

        val sellerId = token?.let { tokenService.extractUserId(it) }
            ?: return ResponseEntity.status(401).build()

        val sale = saleService.create(
            dto.copy(sellerId = sellerId)
        )

        return ResponseEntity.ok(SaleMapper.toResponseDTO(sale))
    }


    /**
     * COMPLETE SALE
     */
    @PostMapping("/completed/{idSale}")
    fun complete(
        @PathVariable idSale: Long,
        @RequestBody dto: SaleDTO
    ): ResponseEntity<SaleResponseDTO> {

        val sale = saleService.completeSale(idSale, dto.observation ?: "")
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(SaleMapper.toResponseDTO(sale))
    }

    /**
     * CANCEL SALE
     */
    @PostMapping("/cancelled/{idSale}")
    fun cancel(@PathVariable idSale: Long): ResponseEntity<String> =
        if (saleService.cancelSale(idSale))
            ResponseEntity.ok("Venda cancelada com sucesso")
        else
            ResponseEntity.notFound().build()

    /**
     * UPLOAD IMAGES
     */
    @PostMapping("/img/{idSale}")
    fun uploadImages(
        @PathVariable idSale: Long,
        @RequestBody batch: ImageBatchDTO
    ): ResponseEntity<Any> {

        val uploadedImages = imageSaleService.uploadImages(idSale, batch)

        return if (uploadedImages.isNotEmpty()) {
            ResponseEntity.ok(
                mapOf(
                    "message" to "Imagens cadastradas com sucesso",
                    "uploaded" to uploadedImages
                )
            )
        } else {
            ResponseEntity.badRequest().body(
                mapOf("error" to "Nenhuma imagem foi cadastrada")
            )
        }
    }

    @DeleteMapping("/img/{idImageSale}")
    fun deleteImage(@PathVariable idImageSale: Long): ResponseEntity<String> =
        if (imageSaleService.deleteImage(idImageSale))
            ResponseEntity.ok("Imagem removida com sucesso")
        else
            ResponseEntity.notFound().build()

    /**
     * GET /{id}
     */
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<SaleResponseDTO> {
        val sale = saleService.findFull(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(SaleMapper.toResponseDTO(sale))
    }

    /**
     * GET ALL
     */
    @GetMapping
    fun findAll(): ResponseEntity<List<SaleResponseDTO>> =
        ResponseEntity.ok(
            saleService.findAll().map { SaleMapper.toResponseDTO(it) }
        )

    /**
     * GET BY STATUS
     */
    @GetMapping("/status/{status}")
    fun findAllstatus(@PathVariable status: Int): ResponseEntity<List<SaleResponseDTO>> =
        ResponseEntity.ok(
            saleService.findAllstatus(status).map { SaleMapper.toResponseDTO(it) }
        )

    /**
     * GET BY SELLER
     */
    @GetMapping("/seller/{seller}")
    fun findAllseller(@PathVariable seller: Long): ResponseEntity<List<SaleResponseDTO>> =
        ResponseEntity.ok(
            saleService.findBySeller(seller).map { SaleMapper.toResponseDTO(it) }
        )

    /**
     * GET BY DATE RANGE
     */
    @GetMapping("/date")
    fun findByDate(
        @RequestParam("start_date") start: String,
        @RequestParam("end_date") end: String
    ): ResponseEntity<Any> {

        return try {
            val startDate = LocalDate.parse(start)
            val endDate = LocalDate.parse(end)

            if (endDate.isBefore(startDate)) {
                return ResponseEntity.badRequest().body(
                    mapOf("error" to "A data final não pode ser menor que a inicial.")
                )
            }

            val sales = saleService.findByPeriod(startDate, endDate)

            ResponseEntity.ok(sales.map { SaleMapper.toResponseDTO(it) })

        } catch (e: Exception) {

            ResponseEntity.badRequest().body(
                mapOf(
                    "error" to "Data inválida.",
                    "details" to e.message
                )
            )
        }
    }
}


