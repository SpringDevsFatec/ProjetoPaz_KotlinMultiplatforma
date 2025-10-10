package com.projetopaz.kotlin.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "O nome da categoria não pode ser vazio.")
    @field:Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    var name: String?,

    @field:Size(max = 255, message = "A descrição não pode exceder 255 caracteres.")
    var description: String?,

    var active: Boolean = true,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null
)