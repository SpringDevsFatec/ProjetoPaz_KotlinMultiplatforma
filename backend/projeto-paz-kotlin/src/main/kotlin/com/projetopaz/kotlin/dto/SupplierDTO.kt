package com.projetopaz.kotlin.dto

data class SupplierDTO(
    val id: Long? = null,
    val name: String? = null,
    val cnpj: String? = null,
    val type: String? = null,
    val occupation: String? = null,
    val observation: String? = null,
    val addresses: List<SupplierAddressDTO> = listOf(),
    val cellphones: List<CellphoneSupplierDTO> = listOf()
)
