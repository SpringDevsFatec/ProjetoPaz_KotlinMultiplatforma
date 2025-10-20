package com.projetopaz.kotlin.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

@Entity
data class SupplierAddress (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:Length(min = 8, max = 8, message = "CEP precisa ter 8 digitos")
    @field:NotEmpty(message = "O cep precisa ser preenchido")
    @field:Column(length = 8)
    var cep: String,

    @field:Column(length = 100)
    var street: String?,

    @field:Column(length = 6)
    var number: String?,

    @field:Column(length = 20)
    var complement: String?,

    @field:Column(length = 100)
    var quartier: String?,

    var status : Boolean,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime?,

    @JsonBackReference
    @OneToOne(mappedBy = "address")
    val supplier: Supplier? = null,
) {
    constructor():this(null, cep="", street=null, number=null, complement=null,
        quartier=null, status=true, createdAt=LocalDateTime.now(), updatedAt=null
    )
}


