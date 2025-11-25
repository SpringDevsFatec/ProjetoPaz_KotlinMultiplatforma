package com.projetopaz.kotlin.util

import com.projetopaz.kotlin.config.AwsProperties
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import java.util.*
import java.util.regex.Pattern

@Component
class S3ImageUploader(
    private val s3Client: S3Client,
    private val awsProperties: AwsProperties
) {
    fun uploadBase64(base64: String): String {

        val matcher = Pattern.compile("^data:(image/\\w+);base64,").matcher(base64)
        require(matcher.find()) { "Formato Base64 inválido. Deve conter o prefixo data:image/png;base64," }

        val mimeType = matcher.group(1)
        val extension = mimeType.substringAfter("/")
        val cleanBase64 = base64.replace("^data:image/\\w+;base64,".toRegex(), "")
        val bytes = Base64.getDecoder().decode(cleanBase64)

        val fileName = "img_${UUID.randomUUID()}.$extension"
        val bucket = awsProperties.bucketUser // ✅ atualizado

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .contentType(mimeType)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes))

        return "https://$bucket.s3.${awsProperties.region}.amazonaws.com/$fileName"
    }
}
