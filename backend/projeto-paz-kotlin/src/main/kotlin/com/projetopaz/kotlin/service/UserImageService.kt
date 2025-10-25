package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.ImageUploadDTO
import com.projetopaz.kotlin.model.User
import com.projetopaz.kotlin.repository.UserRepository
import com.projetopaz.kotlin.util.S3ImageUploader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserImageService(
    private val userRepository: UserRepository,
    private val uploader: S3ImageUploader
) {
    private val logger = LoggerFactory.getLogger(UserImageService::class.java)

    fun uploadUserImage(id: Long, dto: ImageUploadDTO): User? {
        val user = userRepository.findById(id).orElse(null) ?: return null

        logger.info("🖼️ Iniciando upload da imagem para o usuário ID $id...")
        val url = uploader.uploadBase64(dto.base64)
        logger.info("✅ Upload concluído. URL: $url")

        user.urlImage = url
        return userRepository.save(user)
    }
}
