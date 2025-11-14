package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.mapper.ProductMapper
import com.projetopaz.kotlin.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val mapper : ProductMapper
) {

    fun findAll(): List<ProductDTOView> {
        val result = productRepository.findActiveProducts()
        return result.map { projection ->
            ProductDTOView(
                id = projection.getId(),
                name = projection.getName(),
                salePrice = projection.getSalePrice(),
                categories = projection.getCategoryNames()?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                favoriteImageUrl = projection.getFavoriteImageUrl()
            )
        }
    }

    fun findById(id: Long): ProductDTO? {
        val productDetails = productRepository.findProductDetailsById(id) ?: return null
        val images = productRepository.findProductImages(id)

        return ProductDTO(
            id = productDetails.getId(),
            name = productDetails.getName(),
            description = productDetails.getDescription(),
            costPrice = productDetails.getCostPrice(),
            salePrice = productDetails.getSalePrice(),
            isFavorite = productDetails.getIsFavorite(),
            isDonation = productDetails.getIsDonation(),
            categoryIds = parseCategoryIds(productDetails.getCategoryIDs()), // ← Função corrigida
            stock = StockDTO(
                quantity = productDetails.getStockQuantity() ?: 0, // ← Default value
                fabrication = productDetails.getFabricationDate(),
                maturity = productDetails.getMaturityDate()
            ),
            supplier = productDetails.getSupplierID()?.toLong()
            // images permanecem comentadas
        )
    }

    private fun parseCategoryIds(ids: String?): List<Long> {
        if (ids.isNullOrBlank()) return emptyList()

        return ids.split(",")
            .mapNotNull { it.trim().toLongOrNull() }
            .filter { it > 0 }
    }

    @Transactional
    fun save(dto: ProductDTO): ProductDTO {
        try {
            println("💾 Iniciando save do produto: ${dto.name}")
            val product = mapper.fromDTO(dto)
            println("📦 Produto preparado para save, tentando persistir...")
            val saved = productRepository.save(product)
            println("✅ Produto salvo com ID: ${saved.id}")
            return mapper.toDTO(saved)
        } catch (e: Exception) {
            println("❌ Erro ao salvar produto: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
    /*

    fun update(id: Long, productDetails: Product): Product {
        val existingProduct = findById(id)

        val categoryId = productDetails.category.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto precisa ter um ID de categoria.")

        val existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID $categoryId não existe.") }

        val updatedProduct = existingProduct.copy(
            name = productDetails.name,
            description = productDetails.description,
            sku = productDetails.sku,
            price = productDetails.price,
            active = productDetails.active,
            category = existingCategory,
            updatedAt = LocalDateTime.now()
        )
        return productRepository.save(updatedProduct)
    }
    */

    /*
    fun delete(id: Long) {
        val product = findById(id)
        product.active = false
        productRepository.save(product)
    }
     */
}