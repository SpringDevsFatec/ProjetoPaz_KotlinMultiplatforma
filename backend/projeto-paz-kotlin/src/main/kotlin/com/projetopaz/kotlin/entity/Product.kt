package com.projetopaz.kotlin.entity

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

    @field:NotBlank(message = "O SKU do produto não pode ser vazio.")
    var sku: String,

    @field:Positive(message = "O preço deve ser um valor positivo.")
    var price: BigDecimal,

    var active: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true) // nullable = true significa que um produto pode não ter um fornecedor
    var supplier: Supplier?,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null
)