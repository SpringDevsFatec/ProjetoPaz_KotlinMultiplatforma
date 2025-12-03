package com.projetopaz.kotlin.controller


import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.log.RequestLoggingFilter
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.service.UserService
import com.projetopaz.kotlin.service.UserImageService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory


@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userImageService: UserImageService
) {
    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    @PostMapping("/recover-password")
    fun recoverPassword(@RequestBody dto: UserRecoveryDTO): ResponseEntity<String> {
        val ok = userService.requestPasswordRecovery(dto.email)

        return if (ok)
            ResponseEntity.ok("Email de recuperação enviado!")
        else
            ResponseEntity.badRequest().body("Email não encontrado.")
    }

    @PutMapping("/reset-password")
    fun resetPassword(@RequestBody dto: UserResetPasswordDTO): ResponseEntity<String> {
        val ok = userService.resetPassword(dto.email, dto.token, dto.newPassword)

        return if (ok)
            ResponseEntity.ok("Senha alterada com sucesso!")
        else
            ResponseEntity.badRequest().body("Token inválido ou email incorreto.")
    }


    @PostMapping
    fun createUser(@Valid @RequestBody dto: UserCreateDTO): ResponseEntity<UserResponseDTO> {

        val user = userService.createUserEncoded(dto)

        println(UserMapper.toResponseDTO(user))
        return ResponseEntity.ok(UserMapper.toResponseDTO(user))
    }


    @GetMapping
    fun getUser(@RequestParam id: Long): ResponseEntity<UserResponseDTO> {
        val user = userService.getById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserMapper.toResponseDTO(user))
    }


    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<UserResponseDTO>> {
        val users = userService.getAll().map { UserMapper.toResponseDTO(it) }
        return ResponseEntity.ok(users)
    }


    @PutMapping
    fun updateUser(@RequestParam id: Long, @Valid @RequestBody dto: UserCreateDTO): ResponseEntity<UserResponseDTO> {
        val updated = userService.updateUser(id, dto) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserMapper.toResponseDTO(updated))
    }


    @DeleteMapping
    fun deleteUser(@RequestParam id: Long): ResponseEntity<String> {
        return if (userService.deleteLogic(id))
            ResponseEntity.ok("Usuário desativado com sucesso")
        else
            ResponseEntity.notFound().build()
    }


    // ✅ Agora usa o service correto para upload de imagem
    @PostMapping("/img")
    fun uploadUserImage(
        @RequestParam id: Long,
        @RequestBody dto: ImageUploadDTO
    ): ResponseEntity<UserResponseDTO> {
        val user = userImageService.uploadUserImage(id, dto)
            ?: return ResponseEntity.notFound().build()


        return ResponseEntity.ok(UserMapper.toResponseDTO(user))
    }
}