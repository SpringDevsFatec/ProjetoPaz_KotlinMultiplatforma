package com.projetopaz.kotlin.dto

import com.projetopaz.kotlin.model.Adress
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Past
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

data class UserCreateDTO(
    @field:NotEmpty(message = "O nome é obrigatório")
    val name: String,

    val surname: String? = null,

    @field:Past(message = "A data de nascimento deve ser no passado")
    val birthday: LocalDate,

    @field:Email(message = "Informe um email válido")
    @field:NotEmpty(message = "O email é obrigatório")
    val email: String,

    @field:Length(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    val password: String,

    val urlImage: String? = null,

    val adress: AdressDTO,

    val cellphones: List<CellphonesDTO> = listOf()
) {
    constructor() : this("", null, LocalDate.now(), "", "", null, AdressDTO(), listOf())
}
