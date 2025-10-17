package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.entity.Supplier
import com.projetopaz.kotlin.service.SupplierService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/supplier")
class SupplierController(
    private val supplierService: SupplierService
) {

    @GetMapping()
    fun getAllSuppliers(): ResponseEntity<List<Supplier>> {
        return ResponseEntity.ok(supplierService.findAll())
    }

    @PostMapping()
    fun createSupplier(@Valid @RequestBody supplier: Supplier): ResponseEntity<Supplier> {
        val savedSupplier = supplierService.save(supplier)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier)
    }

    @GetMapping("/{id}")
    fun getSupplierById(@PathVariable("id") id: Long): ResponseEntity<Supplier> {
        return ResponseEntity.ok(supplierService.findById(id))
    }

    @PutMapping("/{id}")
    fun updateSupplier(@PathVariable("id") id: Long, @Valid @RequestBody supplierDetails: Supplier): ResponseEntity<Supplier> {
        val updatedSupplier = supplierService.update(id, supplierDetails)
        return ResponseEntity.ok(updatedSupplier)
    }

    @DeleteMapping("/{id}")
    fun deleteSupplier(@PathVariable("id") id: Long): ResponseEntity<Void> {
        supplierService.delete(id)
        return ResponseEntity.noContent().build()
    }
}