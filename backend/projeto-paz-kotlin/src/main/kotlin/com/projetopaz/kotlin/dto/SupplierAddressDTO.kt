package com.projetopaz.kotlin.dto

data class SupplierAddressDTO(
    val id: Long? = null,
    val street: String? = null,
    val number: String? = null,
    val complement: String? = null,
    val neighborhood: String? = null,
    val cep: String? = null,
    val city: String? = null,
    val state: String? = null
)
