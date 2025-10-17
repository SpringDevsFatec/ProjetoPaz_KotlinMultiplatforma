package com.projetopaz.kotlin.entity

enum class StatusSupplierAddress(val description: String) {
    ACTIVE("Ativo"),
    DISABLED("Desativado");

    fun isActive() = this == ACTIVE
}