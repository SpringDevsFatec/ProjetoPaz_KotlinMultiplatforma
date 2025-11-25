package com.projetopaz.kotlin.dto

data class SupplierResponseDTO(
    val id: Long?,
    val name: String?,
    val cnpj: String?,
    val type: String?,
    val occupation: String?,
    val observation: String?,
    val status: Int
)
