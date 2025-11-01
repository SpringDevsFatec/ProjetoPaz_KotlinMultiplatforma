package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.entity.Product
import com.projetopaz.kotlin.mapper.ProductMapper
import com.projetopaz.kotlin.repository.CategoryRepository
import com.projetopaz.kotlin.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
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
            categories = parseCategories(productDetails.getCategoryIDs()),
            stock = StockDTO(
                quantity = productDetails.getStockQuantity(),
                fabrication = productDetails.getFabricationDate(),
                maturity = productDetails.getMaturityDate()
            ),
            supplier = productDetails.getSupplierID(),
            images = images.map { image ->
                ProductImageDTO(
                    id = image.getImageId(),
                    url = image.getImageUrl(),
                    altText = image.getAltText(),
                    isFavorite = image.getIsFavorite()
                )
            }
        )
    }

    private fun parseCategories(ids: String?): List<CategoryDTOViewIds> {
        if (ids.isNullOrBlank()) return emptyList()

        val idList = ids.split(",")

        return idList.mapIndexed { index, id ->
            CategoryDTOViewIds(
                id = id.toLong(),
            )
        }
    }

    /*
    fun findById(id: Long): ProductDTO {
        val product = productRepository.findById(id).orElseThrow {
            IllegalStateException("Produto não encontrado: $id")
        }
        return mapper.toDTO(product)
    }
    */

    /*
    fun save(product: Product): Product {
        val categoryId = product.category.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto precisa ter um ID de categoria.")

        val existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria com ID $categoryId não existe.") }

        product.category = existingCategory
        return productRepository.save(product)
    }
    */
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