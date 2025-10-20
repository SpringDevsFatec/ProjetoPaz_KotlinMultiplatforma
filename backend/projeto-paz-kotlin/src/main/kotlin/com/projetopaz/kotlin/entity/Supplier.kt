package com.projetopaz.kotlin.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonManagedReference

@Entity
data class Supplier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "O nome do fornecedor não pode ser vazio.")
    @field:Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    var name: String,

    var contactName: String?,

    @field:Email(message = "O formato do email é inválido.")
    var email: String?,

    var active: Boolean = true,

    var cnpj: String?,

    var type: String?,

    var observation: String?,

    var occupation: String?,

    var createUser: Long?,

    var updateUser: Long?,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null,

    @JsonManagedReference
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    val phone: SupplierCellphone? = null,

    @JsonManagedReference
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    val address: SupplierAddress? = null
) {
    constructor():this(id=null, name="", contactName=null, email=null, active=true, cnpj=null,
        type=null, observation=null, occupation=null, createUser=null, updateUser=null,
        createdAt=LocalDateTime.now(), updatedAt=LocalDateTime.now(),
        phone=SupplierCellphone(), address=SupplierAddress()
    )
}