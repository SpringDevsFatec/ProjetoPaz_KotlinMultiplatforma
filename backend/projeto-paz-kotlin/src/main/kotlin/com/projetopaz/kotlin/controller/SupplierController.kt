package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.dto.SupplierDTOView
import com.projetopaz.kotlin.dto.SupplierStatusDTO
import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.service.SupplierService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/supplier")
class SupplierController(
    private val service: SupplierService
) {

    @GetMapping()
    fun getAllSuppliers(): ResponseEntity<List<SupplierDTOView>> {
        return ResponseEntity.ok(service.findAll())
    }

    @PostMapping()
    fun createSupplier(@Valid @RequestBody supplier: SupplierDTO): ResponseEntity<SupplierDTO> {
        val savedSupplier = service.save(supplier)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier)
    }

    @GetMapping("/{id}")
    fun getSupplierById(@PathVariable("id") id: Long): ResponseEntity<SupplierDTO> {
        return ResponseEntity.ok(service.findById(id))
    }

    @PutMapping("/{id}")
    fun updateSupplier(@PathVariable("id") id: Long, @Valid @RequestBody supplierDetails: SupplierDTO): ResponseEntity<SupplierDTO> {
        val updatedSupplier = service.update(id, supplierDetails)
        return ResponseEntity.ok(updatedSupplier)
    }

    @PatchMapping("/{id}/active")
    fun deleteSupplier(@PathVariable("id") id: Long, @RequestBody dto: SupplierStatusDTO): ResponseEntity<SupplierDTO> {
        return ResponseEntity.ok(service.delete(id, dto))
    }
}