package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.model.*
import java.time.LocalDate

object UserMapper {

    // Criação de entidade a partir do DTO de criação (sem ID)
    fun toEntity(dto: UserCreateDTO): User {
        val user = User(
            name = dto.name,
            surname = dto.surname ?: "",
            birthday = dto.birthday,
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

    // Atualiza entidade existente a partir do DTO de criação (sem alterar senha se quiser)
    fun updateEntity(entity: User, dto: UserCreateDTO) {
        entity.apply {
            name = dto.name
            surname = dto.surname ?: ""
            birthday = dto.birthday
            email = dto.email
            password = dto.password
            urlImage = dto.urlImage
            updatedAt = LocalDate.now()
        }

        // Endereço
        dto.adress.let { addrDto ->
            if (entity.adress == null) {
                entity.adress = Adress(
                    street = addrDto.street,
                    cep = addrDto.cep,
                    quarter = addrDto.quarter,
                    number = addrDto.number,
                    complement = addrDto.complement,
                    user = entity
                )
            } else {
                entity.adress!!.apply {
                    street = addrDto.street
                    cep = addrDto.cep
                    quarter = addrDto.quarter
                    number = addrDto.number
                    complement = addrDto.complement
                    updatedAt = LocalDate.now()
                }
            }
        }

        // Telefones
        entity.cellphones.clear()
        entity.cellphones.addAll(dto.cellphones.map {
            Cellphones(
                countryNumber = it.countryNumber,
                ddd1 = it.ddd1,
                ddd2 = it.ddd2,
                cellphone1 = it.cellphone1,
                cellphone2 = it.cellphone2,
                user = entity
            )
        })
    }

    // (não expõe senha)
    fun toResponseDTO(entity: User): UserResponseDTO = UserResponseDTO(
        id = entity.id,
        name = entity.name,
        surname = entity.surname,
        birthday = entity.birthday,
        email = entity.email,
        urlImage = entity.urlImage,
        adress = entity.adress?.let {
            AdressDTO(
                street = it.street,
                cep = it.cep,
                quarter = it.quarter,
                number = it.number,
                complement = it.complement
            )
        },
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
