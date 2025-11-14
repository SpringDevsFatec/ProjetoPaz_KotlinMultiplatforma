package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.ProductDTO
import com.projetopaz.kotlin.dto.StockDTO
import com.projetopaz.kotlin.entity.Category
import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.entity.Stock
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.service.SupplierService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ProductMapper(
    private val categoryRepository: CategoryRepository,
    private val supplierService: SupplierService
) {
    fun fromDTO(dto: ProductDTO): Product {
        println("🚀 Iniciando mapeamento do produto: ${dto.name}")

        // Validar categorias
        val categories = validateAndGetCategories(dto.categoryIds)
        println("✅ Categorias carregadas: ${categories.size}")

        // Supplier opcional
        val supplier = dto.supplier?.let {
            println("🔍 Buscando supplier ID: $it")
            val foundSupplier = supplierService.findByIdNoDto(it)
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor não encontrado: $it")
            println("✅ Supplier encontrado: ${foundSupplier.name}")
            foundSupplier
        }

        // Validar stock
        val stock = validateAndCreateStock(dto.stock)
        println("✅ Stock criado: quantidade=${stock.quantity}")

        val product = Product(
            id = dto.id,
            name = dto.name.trim(),
            description = dto.description?.trim(),
            costPrice = dto.costPrice,
            salePrice = dto.salePrice,
            isFavorite = dto.isFavorite,
            isDonation = dto.isDonation,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            categories = categories.toMutableSet(),
            supplier = supplier,
            stock = stock,
            images = mutableListOf()
        )

        println("🎉 Produto mapeado com sucesso: ${product.name}")
        return product
    }

    private fun validateAndGetCategories(categoryIds: List<Long>): List<Category> {
        if (categoryIds.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Pelo menos uma categoria é obrigatória")
        }

        // Busca única com distinct para evitar duplicatas
        val categories = categoryRepository.findAllById(categoryIds).distinctBy { it.id }

        // Verificar se todas as categorias foram encontradas
        if (categories.size != categoryIds.distinct().size) {
            val foundIds = categories.map { it.id!! }
            val missingIds = categoryIds - foundIds
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Categorias não encontradas: $missingIds")
        }

        // Verificar se todas estão ativas
        val inactiveCategories = categories.filter { !it.active }
        if (inactiveCategories.isNotEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Categorias inativas: ${inactiveCategories.map { it.id }}")
        }

        return categories
    }

    private fun validateAndCreateStock(stockDTO: StockDTO?): Stock {
        if (stockDTO == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque é obrigatório")
        }

        if (stockDTO.quantity < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade em estoque não pode ser negativa")
        }

        return Stock(
            quantity = stockDTO.quantity,
            fabrication = stockDTO.fabrication ?: LocalDate.now(),
            maturity = stockDTO.maturity ?: LocalDate.now().plusMonths(6), // Default 6 meses
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )
    }

    private fun validatePrices(costPrice: BigDecimal, salePrice: BigDecimal) {
        if (costPrice <= BigDecimal.ZERO) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço de custo deve ser positivo")
        }

        if (salePrice <= BigDecimal.ZERO) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço de venda deve ser positivo")
        }

        if (salePrice < costPrice) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço de venda não pode ser menor que o preço de custo")
        }
    }

    private fun validateStockDates(fabrication: LocalDate?, maturity: LocalDate?) {
        if (fabrication != null && maturity != null && fabrication.isAfter(maturity)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de fabricação não pode ser após a data de validade")
        }

        if (maturity != null && maturity.isBefore(LocalDate.now())) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de validade não pode ser no passado")
        }
    }

    fun toDTO(entity: Product): ProductDTO {
        return ProductDTO(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            costPrice = entity.costPrice,
            salePrice = entity.salePrice,
            isFavorite = entity.isFavorite,
            isDonation = entity.isDonation,
            categoryIds = entity.categories.map { it.id!! },
            supplier = entity.supplier?.id,
            stock = entity.stock?.let {
                StockDTO(
                    quantity = it.quantity,
                    fabrication = it.fabrication,
                    maturity = it.maturity
                )
            }
        )
    }
}