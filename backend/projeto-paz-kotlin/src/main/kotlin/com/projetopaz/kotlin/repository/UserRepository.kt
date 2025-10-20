package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmailAndStatusTrue(email: String): User?
    fun findAllByStatusTrue(): List<User>
}