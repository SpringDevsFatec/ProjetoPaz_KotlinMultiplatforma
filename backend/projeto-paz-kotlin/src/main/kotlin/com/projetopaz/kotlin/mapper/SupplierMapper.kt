package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.SupplierAddressDTO
import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.dto.SupplierPhoneDTO
import com.projetopaz.kotlin.dto.SupplierDTOView
import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.entity.SupplierAddress
import com.projetopaz.kotlin.entity.SupplierCellphone
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SupplierMapper {

    fun fromDTO(dto: SupplierDTO): Supplier {
        val phone = SupplierCellphone(
            countryNumber = dto.phone?.countryNumber ?: "",
            ddd1 = dto.phone?.ddd1 ?: "",
            ddd2 = dto.phone?.ddd2,
            cellphone1 = dto.phone?.cellphone1 ?: "",
            cellphone2 = dto.phone?.cellphone2,
            createdAt = LocalDateTime.now(),
        )
        val address = SupplierAddress(
            cep = dto.address?.cep ?: "",
            street = dto.address?.street,
            number = dto.address?.number,
            complement = dto.address?.complement,
            quartier = dto.address?.quartier,
            status = true,
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )

        return Supplier(
            id = dto.id,
            name = dto.name,
            contactName = null,
            email = null,
            active = true,
            cnpj = dto.cnpj,
            type = dto.type,
            observation = dto.observation,
            occupation = null,
            createUser = null,
            updateUser = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            phone = phone,
            address = address,
        )
    }

    fun toDTO(entity:Supplier): SupplierDTO {
        return SupplierDTO(
            id = entity.id,
            name = entity.name,
            cnpj = entity.cnpj,
            type = entity.type,
            observation = entity.observation,
            phone = SupplierPhoneDTO(
                countryNumber = entity.phone?.countryNumber ?: "",
                ddd1 = entity.phone?.ddd1 ?: "",
                cellphone1 = entity.phone?.cellphone1 ?: "",
                ddd2 = entity.phone?.ddd2 ?: "",
                cellphone2 = entity.phone?.cellphone2 ?: "",
            ),
            address = SupplierAddressDTO(
                cep = entity.address?.cep ?: "",
                street = entity.address?.street,
                number = entity.address?.number,
                complement = entity.address?.complement,
                quartier = entity.address?.quartier,
            )
        )
    }

    fun toDTOView(entity: Supplier): SupplierDTOView {
        return SupplierDTOView(
            id = entity.id,
            name = entity.name
        )
    }
}