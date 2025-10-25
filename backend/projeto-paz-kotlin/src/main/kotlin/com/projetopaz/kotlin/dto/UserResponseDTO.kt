package com.projetopaz.kotlin.dto

import java.time.LocalDate

data class UserResponseDTO(
    val id: Long,
    val name: String,
    val surname: String?,
    val birthday: LocalDate,
    val email: String,
    val urlImage: String?,
    val adress: AdressDTO?,
    val cellphones: List<CellphonesDTO>?
)
