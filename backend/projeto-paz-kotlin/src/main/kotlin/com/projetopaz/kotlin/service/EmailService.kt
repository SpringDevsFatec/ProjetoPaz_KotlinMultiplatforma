package com.projetopaz.kotlin.service

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    private val dotenv: Dotenv = Dotenv.configure()
        .directory(Paths.get("").toAbsolutePath().resolve("backend/projeto-paz-kotlin").toString())
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    fun sendPasswordRecoveryEmail(email: String, token: String) {

        val link = "https://seusite.com/redefinir-senha?token=$token&email=$email"

        val html = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                <div style="max-width: 480px; margin: auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08);">
                    
                    <h2 style="color: #333; text-align: center; margin-bottom: 20px;">
                        Recuperação de Senha
                    </h2>
                    
                    <p style="color: #555; font-size: 15px;">
                        Olá! Recebemos uma solicitação para redefinir sua senha no 
                        <strong>Projeto Paz</strong>. Clique no botão abaixo para continuar:
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="$link" style="
                            background-color: #4CAF50;
                            color: white;
                            padding: 12px 24px;
                            text-decoration: none;
                            font-size: 16px;
                            border-radius: 8px;
                            display: inline-block;">
                            Redefinir Senha
                        </a>
                    </div>

                    <p style="color: #777; font-size: 13px;">
                        Se você não pediu isso, pode ignorar este e-mail com segurança.
                    </p>

                    <hr style="border: 0; border-top: 1px solid #eee; margin: 25px 0;" />

                    <p style="color: #999; font-size: 12px; text-align: center;">
                        © 2025 Projeto Paz. Todos os direitos reservados.
                    </p>
                </div>
            </body>
            </html>
        """.trimIndent()

        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("Recuperação de Senha - Projeto Paz")
        helper.setText(html, true)
        helper.setFrom(dotenv["MAIL_FROM"])

        mailSender.send(mimeMessage)
    }
}
