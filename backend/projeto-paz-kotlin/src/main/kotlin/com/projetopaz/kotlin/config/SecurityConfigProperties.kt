package com.projetopaz.kotlin.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths

@Configuration
class SecurityConfigProperties {

    private val dotenv: Dotenv = Dotenv.configure()
        .directory(Paths.get("").toAbsolutePath().resolve("backend/projeto-paz-kotlin").toString())
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    private val securityProps = SecurityProperties(
        tokenSecretKey = dotenv["TOKEN_SECRET_KEY"] ?: "",
        tokenExpirationSeconds = dotenv["TOKEN_EXPIRATION_TIME"]?.toLongOrNull() ?: 3600
    )

    init {
        require(securityProps.tokenSecretKey.isNotBlank()) { "TOKEN_SECRET_KEY não definido no .env" }
    }

    @Bean
    fun securityProperties(): SecurityProperties = securityProps
}