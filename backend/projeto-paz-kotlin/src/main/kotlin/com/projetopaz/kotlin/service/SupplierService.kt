package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.repository.SupplierRepository
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

        val updatedPhone = existingSupplier.phone?.let { old ->
            old.copy(
                countryNumber = supplierDetails.phone?.countryNumber ?: old.countryNumber,
                ddd1 = supplierDetails.phone?.ddd1 ?: old.ddd1,
                ddd2 = supplierDetails.phone?.ddd2 ?: old.ddd2,
                cellphone1 = supplierDetails.phone?.cellphone1 ?: old.cellphone1,
                cellphone2 = supplierDetails.phone?.cellphone2 ?: old.cellphone2
            )
        }

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
            email = supplierDetails.email,
            active = supplierDetails.active,
            updatedAt = LocalDateTime.now(),
            cnpj = supplierDetails.cnpj,
            type = supplierDetails.type,
            observation = supplierDetails.observation,
            occupation = supplierDetails.occupation,
            updateUser = supplierDetails.updateUser,
            phone = updatedPhone,
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