package com.projetopaz.kotlin.dto

data class UserDTO(
    val id: Long? = null,
    val name: String,
    val surname: String,
    val birthday: String,
    val email: String,
    val password: String,
    val urlImage: String? = null,
    val adress: AdressDTO,
    val cellphones: List<CellphonesDTO>
)