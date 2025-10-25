package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.mapper.UserMapper
import com.projetopaz.kotlin.model.User
import com.projetopaz.kotlin.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(dto: UserCreateDTO): User {
        val entity = UserMapper.toEntity(dto)
        return userRepository.save(entity)
    }

    fun getAll(): List<User> = userRepository.findAllByStatusTrue()

    fun getById(id: Long): User? =
        userRepository.findById(id).orElse(null)?.takeIf { it.status }

    fun updateUser(id: Long, dto: UserCreateDTO): User? {
        val user = userRepository.findById(id).orElse(null) ?: return null
        UserMapper.updateEntity(user, dto)
        return userRepository.save(user)
    }

    fun deleteLogic(id: Long): Boolean {
        val user = userRepository.findById(id).orElse(null) ?: return false
        user.status = false
        userRepository.save(user)
        return true
    }

    fun login(dto: UserLoginDTO): User? {
        val user = userRepository.findByEmailAndStatusTrue(dto.email)
        return if (user?.password == dto.password) user else null
    }


}
