package com.projetopaz.kotlin.model

import jakarta.persistence.*

@Entity
@Table(name = "image_product")
data class ImageProduct(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var url: String? = null,

    var alt: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    @Column(nullable = false)
    var status: Int = 1 // 1 ativo, 0 inativo (soft delete)


)
