package com.projetopaz.kotlin.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "product")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    @field:NotBlank(message = "O nome do produto não pode ser vazio.")
    @field:Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    var name: String = "",

    @Column(name = "cost_price")
    @field:Positive(message = "O preço deve ser um valor positivo.")
    var costPrice: Double = 0.0,

    @Column(name = "sale_price")
    @field:Positive(message = "O preço deve ser um valor positivo.")
    var salePrice: Double = 0.0,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "is_favorite")
    var isFavorite: Boolean = false,

    @Column
    var donation: Boolean = false,

    // relacionamento com supplier (muitos produtos para 1 supplier)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    var supplier: Supplier? = null,

    // stock 1:1
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "stock_id")
    var stock: Stock? = null,

    // imagens 1:N
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var images: MutableList<ImageProduct> = mutableListOf(),

    // categorias N:N
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableSet<Category> = mutableSetOf(),

    @Column(nullable = false)
    var status: Int = 1, // 1 ativo, 0 inativo

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "create_user")
    var createUser: Long? = null,

    @Column(name = "update_user")
    var updateUser: Long? = null
)
