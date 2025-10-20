package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.model.Supplier
import com.projetopaz.kotlin.service.SupplierService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory


@RestController
class SupplierController(
    private val supplierService: SupplierService
)
{
    private val logger = LoggerFactory.getLogger(SupplierController::class.java)

    @GetMapping("/api/supplier")
    fun getAllSuppliers(): ResponseEntity<List<Supplier>> {
        return ResponseEntity.ok(supplierService.findAll())
    }

    @PostMapping("/api/supplier")
    fun createSupplier(@Valid @RequestBody supplier: Supplier): ResponseEntity<Supplier> {
        logger.info("JSON supplier: " )
        logger.info("Iniciando criação de supplier: $supplier")

        val savedSupplier = supplierService.save(supplier)

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier)
    }

    @GetMapping("/api/supplier/{id}")
    fun getSupplierById(@PathVariable("id") id: Long): ResponseEntity<Supplier> {
        return ResponseEntity.ok(supplierService.findById(id))
    }

    @PutMapping("/api/supplier/{id}")
    fun updateSupplier(@PathVariable("id") id: Long, @Valid @RequestBody supplierDetails: Supplier): ResponseEntity<Supplier> {
        val updatedSupplier = supplierService.update(id, supplierDetails)

        return ResponseEntity.ok(updatedSupplier)
    }

    @DeleteMapping("/api/supplier/{id}")
    fun deleteSupplier(@PathVariable("id") id: Long): ResponseEntity<Void> {
        supplierService.delete(id)
        return ResponseEntity.noContent().build()
    }
}