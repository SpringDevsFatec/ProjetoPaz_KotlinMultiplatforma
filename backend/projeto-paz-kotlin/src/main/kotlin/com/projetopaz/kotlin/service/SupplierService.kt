package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.mapper.SupplierMapper
import com.projetopaz.kotlin.model.Supplier
import com.projetopaz.kotlin.repository.CellphoneSupplierRepository
import com.projetopaz.kotlin.repository.SupplierAddressRepository
import com.projetopaz.kotlin.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository,
    private val addressRepository: SupplierAddressRepository,
    private val cellphoneRepository: CellphoneSupplierRepository
) {

    @Transactional
    fun create(dto: SupplierDTO): Supplier {
        val supplier = SupplierMapper.toEntity(dto)
        // saving cascade children because @OneToMany cascade = ALL is configured
        return supplierRepository.save(supplier)
    }

    @Transactional
    fun update(id: Long, dto: SupplierDTO): Supplier? {
        val existing = supplierRepository.findByIdAndStatus(id, 1) ?: return null

        existing.name = dto.name
        existing.cnpj = dto.cnpj
        existing.type = dto.type
        existing.occupation = dto.occupation
        existing.observation = dto.observation

        // Simplificação: atualizamos coleções substituindo-as.
        // Alternativa: implementar merge mais fino (atualizar, criar e inativar conforme id).
        // Inativar antigos
        existing.addresses.forEach { it.status = 0 }
        existing.addresses.clear()
        dto.addresses.forEach { aDto ->
            val addr = com.projetopaz.kotlin.model.SupplierAddress(
                street = aDto.street,
                number = aDto.number,
                complement = aDto.complement,
                neighborhood = aDto.neighborhood,
                cep = aDto.cep,
                city = aDto.city,
                state = aDto.state,
                supplier = existing
            )
            existing.addresses.add(addr)
        }

        existing.cellphones.forEach { it.status = 0 }
        existing.cellphones.clear()
        dto.cellphones.forEach { cDto ->
            val cp = com.projetopaz.kotlin.model.CellphoneSupplier(
                countryNumber = cDto.countryNumber,
                ddd1 = cDto.ddd1,
                ddd2 = cDto.ddd2,
                phone1 = cDto.phone1,
                phone2 = cDto.phone2,
                supplier = existing
            )
            existing.cellphones.add(cp)
        }

        return supplierRepository.save(existing)
    }

    @Transactional
    fun deleteLogic(id: Long): Boolean {
        val existing = supplierRepository.findByIdAndStatus(id, 1) ?: return false
        existing.status = 0
        // inativar filhos também (opcional)
        existing.addresses.forEach { it.status = 0 }
        existing.cellphones.forEach { it.status = 0 }
        supplierRepository.save(existing)
        return true
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Supplier> = supplierRepository.findAllByStatus(1)

    @Transactional(readOnly = true)
    fun getById(id: Long): Supplier? = supplierRepository.findByIdAndStatus(id, 1)
}
