package com.projetopaz.kotlin.repository

import com.projetopaz.kotlin.model.Community
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepository : JpaRepository<Community, Long> {
    fun findAllByStatusTrue(): List<Community>
}
