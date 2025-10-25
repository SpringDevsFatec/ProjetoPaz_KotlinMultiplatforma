package com.projetopaz.kotlin.controller


import com.projetopaz.kotlin.dto.UserCreateDTO
import com.projetopaz.kotlin.dto.UserLoginDTO
import com.projetopaz.kotlin.dto.UserResponseDTO
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.service.UserService
import com.projetopaz.kotlin.security.TokenService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val tokenService: TokenService
) {


    @PostMapping("/register")
    fun register(@Valid @RequestBody dto: UserCreateDTO): ResponseEntity<Any> {
        println("ai zé da manga")
        val created = userService.createUserEncoded(dto)
        val token = tokenService.generateToken(created.email)
        return ResponseEntity.ok(mapOf("token" to token, "user" to UserMapper.toResponseDTO(created)))
    }


    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: UserLoginDTO): ResponseEntity<Any> {
        val user = userService.login(dto) ?: return ResponseEntity.status(401).body(mapOf("error" to "Credenciais inválidas"))
        val token = tokenService.generateToken(user.email)
        return ResponseEntity.ok(mapOf("token" to token, "user" to UserMapper.toResponseDTO(user)))
    }
}