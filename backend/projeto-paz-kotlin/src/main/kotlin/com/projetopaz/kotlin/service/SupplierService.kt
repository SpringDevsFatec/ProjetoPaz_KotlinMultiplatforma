package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.entity.SupplierAddress.StatusSupplierAddress
import com.projetopaz.kotlin.repository.SupplierRepository
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository
) {

    // Regra de negócio: Retornar apenas fornecedores ativos
    fun findAll(): List<Supplier> {
        return supplierRepository.findAll().filter { it.active }
    }

    fun findById(id: Long): Supplier {
        return supplierRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor com ID $id não encontrado") }
    }

    fun save(supplier: Supplier): Supplier {
        return supplierRepository.save(supplier)
    }

    @Transactional
    fun update(id: Long, supplierDetails: Supplier): Supplier {
        val existingSupplier = supplierRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Fornecedor não encontrado") }

        val updatedAddress = existingSupplier.address?.let { old ->
            old.copy(
                cep = supplierDetails.address?.cep ?: old.cep,
                street = supplierDetails.address?.street ?: old.street,
                number = supplierDetails.address?.number ?: old.number,
                complement = supplierDetails.address?.complement ?: old.complement,
                quartier = supplierDetails.address?.quartier ?: old.quartier,
                status = supplierDetails.address?.status ?: old.status
            )
        } ?: supplierDetails.address

        val updatedSupplier = existingSupplier.copy(
            name = supplierDetails.name,
            contactName = supplierDetails.contactName,
            phone = supplierDetails.phone,
            email = supplierDetails.email,
            active = supplierDetails.active,
            updatedAt = LocalDateTime.now(),
            location = supplierDetails.location,
            occupation = supplierDetails.occupation,
            updateUser = supplierDetails.updateUser,
            address = updatedAddress
        )

        return supplierRepository.save(updatedSupplier)
    }

    // Regra de negócio: Inativar o fornecedor, não deletar
    fun delete(id: Long) {
        val supplier = findById(id)
        supplier.active = false
        supplierRepository.save(supplier)
    }
}