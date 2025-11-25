package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.model.*

object SupplierMapper {
    // DTO -> Entity
    fun toEntity(dto: SupplierDTO): Supplier {
        val supplier = Supplier(
            name = dto.name,
            cnpj = dto.cnpj,
            type = dto.type,
            occupation = dto.occupation,
            observation = dto.observation
        )

        // addresses
        dto.addresses.forEach { a ->
            val addr = SupplierAddress(
                street = a.street,
                number = a.number,
                complement = a.complement,
                neighborhood = a.neighborhood,
                cep = a.cep,
                city = a.city,
                state = a.state,
                supplier = supplier
            )
            supplier.addresses.add(addr)
        }
        // cellphones
        dto.cellphones.forEach { c ->
            val cp = CellphoneSupplier(
                countryNumber = c.countryNumber,
                ddd1 = c.ddd1,
                ddd2 = c.ddd2,
                phone1 = c.phone1,
                phone2 = c.phone2,
                supplier = supplier
            )
            supplier.cellphones.add(cp)
        }
        return supplier
    }

    // Entity -> DTO
    fun toDto(entity: Supplier): SupplierDTO = SupplierDTO(
        id = entity.id,
        name = entity.name,
        cnpj = entity.cnpj,
        type = entity.type,
        occupation = entity.occupation,
        observation = entity.observation,
        addresses = entity.addresses.map { addressToDto(it) },
        cellphones = entity.cellphones.map { cellphoneToDto(it) }
    )

    fun addressToDto(a: SupplierAddress) = SupplierAddressDTO(
        id = a.id,
        street = a.street,
        number = a.number,
        complement = a.complement,
        neighborhood = a.neighborhood,
        cep = a.cep,
        city = a.city,
        state = a.state
    )

    fun cellphoneToDto(c: CellphoneSupplier) = CellphoneSupplierDTO(
        id = c.id,
        countryNumber = c.countryNumber,
        ddd1 = c.ddd1,
        ddd2 = c.ddd2,
        phone1 = c.phone1,
        phone2 = c.phone2
    )
}
