package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.CommunityCreateDTO
import com.projetopaz.kotlin.dto.CommunityResponseDTO
import com.projetopaz.kotlin.mapper.CommunityMapper
import com.projetopaz.kotlin.repository.CommunityRepository
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val communityRepository: CommunityRepository
) {

    fun create(dto: CommunityCreateDTO): CommunityResponseDTO {
        val entity = CommunityMapper.toEntity(dto)
        val saved = communityRepository.save(entity)
        return CommunityMapper.toResponseDTO(saved)
    }

    fun update(id: Long, dto: CommunityCreateDTO): CommunityResponseDTO? {
        val community = communityRepository.findById(id).orElse(null) ?: return null
        CommunityMapper.updateEntity(community, dto)
        val updated = communityRepository.save(community)
        return CommunityMapper.toResponseDTO(updated)
    }

    fun findAll(): List<CommunityResponseDTO> =
        communityRepository.findAllByStatusTrue().map { CommunityMapper.toResponseDTO(it) }

    fun findById(id: Long): CommunityResponseDTO? =
        communityRepository.findById(id)
            .orElse(null)
            ?.takeIf { it.status }
            ?.let { CommunityMapper.toResponseDTO(it) }

    fun deleteLogic(id: Long): Boolean {
        val community = communityRepository.findById(id).orElse(null) ?: return false
        community.status = false
        communityRepository.save(community)
        return true
    }
}
