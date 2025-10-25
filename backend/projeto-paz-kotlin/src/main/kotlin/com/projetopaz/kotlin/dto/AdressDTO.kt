package com.projetopaz.kotlin.dto


data class AdressDTO(
    val street: String = "",
    val cep: String = "",
    val quarter: String = "",
    val number: String = "",
    val complement: String? = null
)
