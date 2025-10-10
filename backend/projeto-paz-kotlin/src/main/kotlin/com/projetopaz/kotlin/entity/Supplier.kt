package com.projetopaz.kotlin.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
data class Supplier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "O nome do fornecedor não pode ser vazio.")
    @field:Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    var name: String,

    var contactName: String?,

    var phone: String?,

    @field:Email(message = "O formato do email é inválido.")
    var email: String?,

    var active: Boolean = true,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null
)