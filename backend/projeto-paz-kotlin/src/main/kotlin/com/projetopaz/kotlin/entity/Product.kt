package com.projetopaz.kotlin.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "O nome do produto não pode ser vazio.")
    @field:Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    var name: String,

    var description: String?,

    @field:Positive(message = "O preço de custo deve ser um valor positivo.")
    var costPrice: BigDecimal,

    @field:Positive(message = "O preço de venda deve ser um valor positivo.")
    var salePrice: BigDecimal,

    var isFavorite: Boolean,

    var isDonation: Boolean,

    var active: Boolean = true,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableSet<Category> = mutableSetOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    var images: MutableList<ProductImage> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true) // nullable = true significa que um produto pode não ter um fornecedor
    var supplier: Supplier?,

    @JsonManagedReference
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    val stock: Stock?

) {
    constructor():this(id=null, name="", description=null,
        costPrice=BigDecimal.ONE, salePrice=BigDecimal.ONE,
        isFavorite=false, isDonation=false,
        createdAt=LocalDateTime.now(), updatedAt=null,
        categories=mutableSetOf(), supplier=null, stock=Stock()
    )
}