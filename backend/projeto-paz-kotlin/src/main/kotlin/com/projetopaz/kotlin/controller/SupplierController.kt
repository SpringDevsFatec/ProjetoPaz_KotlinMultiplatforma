package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.SupplierDTO
import com.projetopaz.kotlin.mapper.SupplierMapper
import com.projetopaz.kotlin.service.SupplierService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/supplier")
class SupplierController(private val supplierService: SupplierService) {

    @PostMapping
    fun create(@RequestBody dto: SupplierDTO): ResponseEntity<Any> {
        if (dto.name.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório")
        }
        val created = supplierService.create(dto)
        return ResponseEntity.ok(SupplierMapper.toDto(created))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: SupplierDTO): ResponseEntity<Any> {
        val updated = supplierService.update(id, dto) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SupplierMapper.toDto(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return if (supplierService.deleteLogic(id)) ResponseEntity.ok(mapOf("message" to "Fornecedor inativado"))
        else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<SupplierDTO>> =
        ResponseEntity.ok(supplierService.getAll().map { SupplierMapper.toDto(it) })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        val found = supplierService.getById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SupplierMapper.toDto(found))
    }
}
