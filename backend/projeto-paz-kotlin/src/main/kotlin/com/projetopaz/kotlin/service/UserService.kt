package com.projetopaz.kotlin.service


import com.projetopaz.kotlin.dto.UserCreateDTO
import com.projetopaz.kotlin.dto.UserLoginDTO
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.model.User
import com.projetopaz.kotlin.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {


    fun createUser(dto: UserCreateDTO): User {
        val entity = UserMapper.toEntity(dto)
        return userRepository.save(entity)
    }


    // New method used by AuthController: encodes password before saving
    fun createUserEncoded(dto: UserCreateDTO): User {
        val entity = UserMapper.toEntity(dto)
        println("chegou service")
        entity.password = passwordEncoder.encode(dto.password)
        println(entity.password)
        return userRepository.save(entity)
    }


    fun getAll(): List<User> = userRepository.findAllByStatusTrue()


    fun getById(id: Long): User? =
        userRepository.findById(id).orElse(null)?.takeIf { it.status }


    fun updateUser(id: Long, dto: UserCreateDTO): User? {
        val user = userRepository.findById(id).orElse(null) ?: return null
        UserMapper.updateEntity(user, dto)
// if password changed in DTO, encode it
        user.password = passwordEncoder.encode(dto.password)
        return userRepository.save(user)
    }


    fun deleteLogic(id: Long): Boolean {
        val user = userRepository.findById(id).orElse(null) ?: return false
        user.status = false
        userRepository.save(user)
        return true
    }


    // New login method which validates password using BCrypt
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmailAndStatusTrue(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")

        return org.springframework.security.core.userdetails.User
            .withUsername(user.email)
            .password(user.password)
            .authorities(emptyList()) // Sem roles por enquanto
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.status)
            .build()
    }



    // Keep original simple login if needed internally (deprecated)
    fun login(dto: UserLoginDTO): User? {
        val user = userRepository.findByEmailAndStatusTrue(dto.email)
        return if (user != null && passwordEncoder.matches(dto.password, user.password)) user else null
    }

}