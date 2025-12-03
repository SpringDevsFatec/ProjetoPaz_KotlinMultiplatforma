package com.projetopaz.kotlin.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*
import java.nio.file.Paths

@Configuration
class MailConfig {

    private val dotenv: Dotenv = Dotenv.configure()
        .directory(Paths.get("").toAbsolutePath().resolve("backend/projeto-paz-kotlin").toString())
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = dotenv["MAIL_HOST"]
        mailSender.port = dotenv["MAIL_PORT"].toInt()
        mailSender.username = dotenv["MAIL_USERNAME"]
        mailSender.password = dotenv["MAIL_PASSWORD"]

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.ssl.trust"] = dotenv["MAIL_HOST"]

        return mailSender
    }
}
