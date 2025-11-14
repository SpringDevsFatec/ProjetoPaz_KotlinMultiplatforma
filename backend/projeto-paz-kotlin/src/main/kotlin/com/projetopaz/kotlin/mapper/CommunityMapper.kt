package com.projetopaz.kotlin.mapper

import com.projetopaz.kotlin.dto.*
import com.projetopaz.kotlin.model.Community
import java.time.LocalDate

object CommunityMapper {

    // Criação de entidade a partir do DTO
    fun toEntity(dto: CommunityCreateDTO): Community {
        return Community(
            name = dto.name,
            description = dto.description,
            cep = dto.cep,
            quarter = dto.quarter,
            number = dto.number,
            complement = dto.complement
        )
    }

    // Atualização de entidade existente
    fun updateEntity(entity: Community, dto: CommunityCreateDTO) {
        entity.apply {
            name = dto.name
            description = dto.description
            cep = dto.cep
            quarter = dto.quarter
            number = dto.number
            complement = dto.complement
            updatedAt = LocalDate.now()
        }
    }

    // Conversão de entidade para DTO de resposta
    fun toResponseDTO(entity: Community): CommunityResponseDTO {
        return CommunityResponseDTO(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            cep = entity.cep,
            quarter = entity.quarter,
            number = entity.number,
            complement = entity.complement,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
