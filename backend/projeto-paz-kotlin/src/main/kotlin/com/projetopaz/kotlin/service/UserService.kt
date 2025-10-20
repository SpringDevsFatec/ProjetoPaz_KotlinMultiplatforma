package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.UserDTO
import com.projetopaz.kotlin.model.User
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(dto: UserDTO): User {
        val entity = UserMapper.toEntity(dto)
        return userRepository.save(entity)
    }

    fun getAll(): List<User> = userRepository.findAllByStatusTrue()

    fun getById(id: Long): User? =
        userRepository.findById(id).orElse(null)?.takeIf { it.status }

    fun updateUser(id: Long, dto: UserDTO): User? {
        val user = userRepository.findById(id).orElse(null) ?: return null
        val updated = UserMapper.toEntity(dto).copy(id = user.id)
        return userRepository.save(updated)
    }

    fun deleteLogic(id: Long): Boolean {
        val user = userRepository.findById(id).orElse(null) ?: return false
        user.status = false
        userRepository.save(user)
        return true
    }

    fun login(email: String, password: String): User? {
        val user = userRepository.findByEmailAndStatusTrue(email)
        return if (user?.password == password) user else null
    }
}