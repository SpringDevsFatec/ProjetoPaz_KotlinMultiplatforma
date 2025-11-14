package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.CommunityCreateDTO
import com.projetopaz.kotlin.dto.CommunityResponseDTO
import com.projetopaz.kotlin.log.RequestLoggingFilter
import com.projetopaz.kotlin.service.CommunityService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/communities")
class CommunityController(
    private val communityService: CommunityService
) {

    @PostMapping
    fun create(@RequestBody dto: CommunityCreateDTO): ResponseEntity<CommunityResponseDTO> {
        println("chegou controller da community!")
        val response = communityService.create(dto)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CommunityCreateDTO): ResponseEntity<CommunityResponseDTO> {
        println("chegou no put!")
        val response = communityService.update(id, dto) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CommunityResponseDTO>> =
        ResponseEntity.ok(communityService.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CommunityResponseDTO> {
        val response = communityService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteLogic(@PathVariable id: Long): ResponseEntity<String> =
        if (communityService.deleteLogic(id))
            ResponseEntity.ok("Comunidade inativada com sucesso")
        else
            ResponseEntity.notFound().build()
}
