package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.mapper.SupplierMapper
import com.projetopaz.kotlin.model.Supplier
import com.projetopaz.kotlin.model.SupplierAddress
import com.projetopaz.kotlin.model.CellphoneSupplier
import com.projetopaz.kotlin.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository
) {

    @Transactional
    fun create(dto: SupplierDTO): Supplier {
        val supplier = SupplierMapper.toEntity(dto)

        supplier.addresses.forEach { it.supplier = supplier }
        supplier.cellphones.forEach { it.supplier = supplier }

        return supplierRepository.save(supplier)
    }

    @Transactional
    fun update(id: Long, dto: SupplierDTO): Supplier? {
        val existing = supplierRepository.findByIdAndStatus(id, 1) ?: return null

        // Atualiza dados simples
        existing.name = dto.name
        existing.cnpj = dto.cnpj
        existing.type = dto.type
        existing.occupation = dto.occupation
        existing.observation = dto.observation
        existing.updatedAt = Instant.now()

        // Atualiza Endereços (Limpa antigos e cria novos)
        existing.addresses.clear()
        dto.addresses.forEach { aDto ->
            val addr = SupplierAddress(
                street = aDto.street,
                number = aDto.number,
                complement = aDto.complement,
                neighborhood = aDto.neighborhood,
                cep = aDto.cep,
                city = aDto.city,
                state = aDto.state,
                supplier = existing // Vínculo obrigatório
            )
            existing.addresses.add(addr)
        }

        // Atualiza Telefones
        existing.cellphones.clear()
        dto.cellphones.forEach { cDto ->
            val cp = CellphoneSupplier(
                countryNumber = cDto.countryNumber,
                ddd1 = cDto.ddd1,
                ddd2 = cDto.ddd2,
                phone1 = cDto.phone1,
                phone2 = cDto.phone2,
                supplier = existing // Vínculo obrigatório
            )
            existing.cellphones.add(cp)
        }

        return supplierRepository.save(existing)
    }

    @Transactional
    fun deleteLogic(id: Long): Boolean {
        val existing = supplierRepository.findByIdAndStatus(id, 1) ?: return false
        existing.status = 0
        existing.updatedAt = Instant.now()

        existing.addresses.forEach { it.status = 0 }

        supplierRepository.save(existing)
        return true
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Supplier> = supplierRepository.findAllByStatus(1)

    @Transactional(readOnly = true)
    fun getById(id: Long): Supplier? = supplierRepository.findByIdAndStatus(id, 1)
}