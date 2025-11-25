package com.projetopaz.kotlin.dto

data class StockDTO(
    val id: Long? = null,
    val name: String? = null,
    val qtd: Int = 0,
    val maturity: String? = null,
    val fabrication: String? = null
)
