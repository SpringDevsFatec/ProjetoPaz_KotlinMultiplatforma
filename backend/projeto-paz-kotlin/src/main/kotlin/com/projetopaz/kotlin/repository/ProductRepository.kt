package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    @Query("""
        SELECT p.id as id,
            p.name as name, 
            p.sale_price as salePrice,
            GROUP_CONCAT(DISTINCT c.name) as categoryNames,
            (SELECT pi.url FROM product_image pi WHERE pi.product_id = p.id AND pi.is_favorite = true LIMIT 1) as favoriteImageUrl
        FROM product p
        LEFT JOIN product_category pc ON p.id = pc.product_id
        LEFT JOIN category c ON pc.category_id = c.id AND c.active = true
        WHERE p.active = true
        GROUP BY p.id, p.name, p.sale_price
    """, nativeQuery = true)
    fun findActiveProducts(): List<ProductProjection>

    @Query("""
        SELECT 
            p.id as id,
            p.name as name,
            p.description as description,
            p.cost_price as costPrice,
            p.sale_price as salePrice,
            p.is_favorite as isFavorite,
            p.is_donation as isDonation,
            GROUP_CONCAT(DISTINCT c.id) as categoryIDs,
            s.quantity as stockQuantity,
            s.fabrication as fabricationDate,
            s.maturity as maturityDate,
            sp.id as supplierID
        FROM product p
        LEFT JOIN product_category pc ON p.id = pc.product_id
        LEFT JOIN category c ON pc.category_id = c.id AND c.active = true
        LEFT JOIN stock s ON p.stock_id = s.id
        LEFT JOIN supplier sp ON p.supplier_id = sp.id
        WHERE p.id = :productId AND p.active = true
        GROUP BY p.id, p.name, p.description, p.cost_price, p.sale_price, 
                 p.is_favorite, p.is_donation, s.quantity, s.fabrication, 
                 s.maturity, sp.id
    """, nativeQuery = true)
    fun findProductDetailsById(productId: Long): ProductDetailsProjection?

    @Query("""
    SELECT 
        pi.id as imageId,
        pi.url as imageUrl,
        pi.alt_text as altText,
        pi.is_favorite as isFavorite
    FROM product_image pi
    WHERE pi.product_id = :productId
    ORDER BY pi.is_favorite DESC
""", nativeQuery = true)
    fun findProductImages(productId: Long): List<ProductImageProjection>
}

interface ProductProjection {
    fun getId(): Long?
    fun getName(): String
    fun getSalePrice(): BigDecimal
    fun getCategoryNames(): String?
    fun getFavoriteImageUrl(): String?
}

interface ProductDetailsProjection {
    fun getId(): Long
    fun getName(): String
    fun getDescription(): String?
    fun getCostPrice(): BigDecimal
    fun getSalePrice(): BigDecimal
    fun getIsFavorite(): Boolean
    fun getIsDonation(): Boolean
    fun getCategoryIDs(): String?
    fun getStockQuantity(): Int
    fun getFabricationDate(): LocalDate?
    fun getMaturityDate(): LocalDate?
    fun getSupplierID(): Long?
}

interface ProductImageProjection {
    fun getImageId(): Long
    fun getImageUrl(): String
    fun getAltText(): String?
    fun getIsFavorite(): Boolean
}