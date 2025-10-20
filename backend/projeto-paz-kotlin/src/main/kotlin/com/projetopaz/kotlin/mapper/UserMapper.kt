package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.model.*

object UserMapper {

    fun toEntity(dto: UserDTO): User {
        val user = User(
            name = dto.name,
            surname = dto.surname,
            birthday = java.time.LocalDate.parse(dto.birthday),// Lembrar de ARRUMAR
            email = dto.email,
            password = dto.password,
            urlImage = dto.urlImage
        )

        val adress = Adress(
            street = dto.adress.street,
            cep = dto.adress.cep,
            quarter = dto.adress.quarter,
            number = dto.adress.number,
            complement = dto.adress.complement,
            user = user
        )

        val cellphones = dto.cellphones.map {
            Cellphones(
                countryNumber = it.countryNumber,
                ddd1 = it.ddd1,
                ddd2 = it.ddd2,
                cellphone1 = it.cellphone1,
                cellphone2 = it.cellphone2,
                user = user
            )
        }.toMutableList()

        user.adress = adress
        user.cellphones = cellphones
        return user
    }

    fun toDTO(entity: User): UserDTO = UserDTO(
        id = entity.id,
        name = entity.name,
        surname = entity.surname,
        birthday = entity.birthday.toString(),
        email = entity.email,
        password = entity.password,
        urlImage = entity.urlImage,
        adress = entity.adress?.let {
            AdressDTO(
                street = it.street,
                cep = it.cep,
                quarter = it.quarter,
                number = it.number,
                complement = it.complement
            )
        } ?: throw IllegalStateException("Adress não encontrado"),
        cellphones = entity.cellphones.map {
            CellphonesDTO(
                countryNumber = it.countryNumber,
                ddd1 = it.ddd1,
                ddd2 = it.ddd2,
                cellphone1 = it.cellphone1,
                cellphone2 = it.cellphone2
            )
        }
    )
}