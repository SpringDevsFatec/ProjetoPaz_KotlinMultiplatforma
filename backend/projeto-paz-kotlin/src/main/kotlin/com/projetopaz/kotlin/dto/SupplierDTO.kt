package com.projetopaz.kotlin.dto

data class SupplierDTO(
    var id : Long? = null,

    val name : String,

    //val contactName : String?,

    //val email : String? = null,

    var active: Boolean = true,

    val cnpj: String?,

    val type: String?,

    val observation: String?,

   //val occupation: String?,

    val phone : SupplierPhoneDTO?,

    val address : SupplierAddressDTO?
) {
    constructor() : this(id=0, name="", cnpj=null,
        type=null, observation=null,
        phone=null, address=null
    )
}

data class SupplierAddressDTO(
    val cep: String,
    val street: String?,
    val number: String?,
    val complement: String?,
    val quartier: String?
)

data class SupplierPhoneDTO(
    val countryNumber: String,
    val ddd1: String,
    val cellphone1: String,
    val ddd2: String?,
    val cellphone2: String?
)

data class SupplierStatusDTO(
    val active: Boolean
)