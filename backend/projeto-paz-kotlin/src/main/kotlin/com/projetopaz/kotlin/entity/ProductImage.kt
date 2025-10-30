package com.projetopaz.kotlin.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class ProductImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var url: String,

    var active: Boolean,

    var altText: String?,

    var isFavorite: Boolean,

    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    var updatedAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product

    ) {
    constructor():this(null, url="", active=true, altText=null,
        isFavorite=false, createdAt=LocalDateTime.now(), updatedAt=null,
        product=Product())
}