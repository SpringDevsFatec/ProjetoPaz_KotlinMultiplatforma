package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.UserDTO
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody dto: UserDTO): ResponseEntity<UserDTO> {
        val user = userService.createUser(dto)
        return ResponseEntity.ok(UserMapper.toDTO(user))
    }

    @GetMapping
    fun getUser(@RequestParam id: Long): ResponseEntity<UserDTO> {
        val user = userService.getById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserMapper.toDTO(user))
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAll().map { UserMapper.toDTO(it) }
        return ResponseEntity.ok(users)
    }

    @PutMapping
    fun updateUser(@RequestParam id: Long, @RequestBody dto: UserDTO): ResponseEntity<UserDTO> {
        val updated = userService.updateUser(id, dto) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserMapper.toDTO(updated))
    }

    @DeleteMapping
    fun deleteUser(@RequestParam id: Long): ResponseEntity<String> {
        return if (userService.deleteLogic(id))
            ResponseEntity.ok("Usuário desativado com sucesso")
        else
            ResponseEntity.notFound().build()
    }

    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestParam password: String): ResponseEntity<UserDTO> {
        val user = userService.login(email, password) ?: return ResponseEntity.status(401).build()
        return ResponseEntity.ok(UserMapper.toDTO(user))
    }
}