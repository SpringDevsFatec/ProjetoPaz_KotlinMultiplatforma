package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.dto.SupplierDTOView
import com.projetopaz.kotlin.dto.SupplierStatusDTO
import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.mapper.SupplierMapper
import com.projetopaz.kotlin.repository.SupplierRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class SupplierService(
    private val repository: SupplierRepository,
    private val mapper : SupplierMapper
) {

    // Regra de negócio: Retornar apenas fornecedores ativos
    fun findAll(): List<SupplierDTOView> {
        return repository.findActive().map { mapper.toDTOView(it) }
    }

    fun findById(id: Long): SupplierDTO {
        val supplier = repository.findById(id).orElseThrow {
            IllegalArgumentException("Fornecedor não encontrado: $id")
        }
        return mapper.toDTO(supplier)
    }

    fun findByIdNoDto(id: Long): Supplier {
        val supplier = repository.findById(id).orElseThrow {
            IllegalArgumentException("Fornecedor não encontrado: $id")
        }
        return supplier
    }

    @Transactional
    fun save(dto: SupplierDTO): SupplierDTO {
        val entity = mapper.fromDTO(dto)
        val saved = repository.save(entity)
        return mapper.toDTO(saved)
    }

    @Transactional
    fun update(id: Long, dto: SupplierDTO): SupplierDTO {
        val existing  = repository.findById(id)
            .orElseThrow { IllegalArgumentException("Fornecedor não encontrado") }

        existing.name = dto.name
        existing.contactName = ""
        existing.email = ""
        existing.cnpj = dto.cnpj
        existing.type = dto.type
        existing.observation = dto.observation
        existing.occupation = ""
        existing.updateUser = null
        existing.updatedAt = LocalDateTime.now()
        existing.phone?.apply {
            countryNumber = dto.phone?.countryNumber ?: ""
            ddd1 = dto.phone?.ddd1 ?: ""
            ddd2 = dto.phone?.ddd2
            cellphone1 = dto.phone?.cellphone1 ?: ""
            cellphone2 = dto.phone?.cellphone2
        }
        existing.address?.apply {
            cep = dto.address?.cep ?: ""
            street = dto.address?.street
            number = dto.address?.number
            complement = dto.address?.complement
            quartier = dto.address?.quartier
        }

        val saved = repository.save(existing)
        return mapper.toDTO(saved)
    }

    // Regra de negócio: Inativar o fornecedor, não deletar
    @Transactional
    fun delete(id: Long, dto: SupplierStatusDTO): SupplierDTO {
        val supplier = repository.findById(id)
            .orElseThrow { RuntimeException("Fornecedor não encontrado") }

        supplier.active = dto.active

        return mapper.toDTO(repository.save(supplier))
    }

}