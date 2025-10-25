package com.projetopaz.kotlin.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.nio.file.Paths

@Configuration
class AwsConfig {

    private val dotenv: Dotenv = Dotenv.configure()
        .directory(Paths.get("").toAbsolutePath().resolve("backend/projeto-paz-kotlin").toString())
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    private val awsProperties = AwsProperties(
        region = dotenv["AWS_REGION"] ?: "",
        accessKey = dotenv["AWS_ACCESS_KEY_ID"] ?: "",
        secretKey = dotenv["AWS_SECRET_ACCESS_KEY"] ?: "",
        bucketUser = dotenv["AWS_BUCKET_USER"] ?: "",
        bucketProduct = dotenv["AWS_BUCKET_PRODUCT"] ?: "",
        bucketSale = dotenv["AWS_BUCKET_SALE"] ?: ""
    )

    @Bean
    fun s3Client(): S3Client {
        println("🔐 AWS CONFIG:")
        println("Region = ${awsProperties.region}")
        println("AccessKey = ${awsProperties.accessKey.take(5)}****")
        println("BucketUser = ${awsProperties.bucketUser}")

        require(awsProperties.accessKey.isNotBlank()) { "AWS_ACCESS_KEY_ID está vazio!" }
        require(awsProperties.secretKey.isNotBlank()) { "AWS_SECRET_ACCESS_KEY está vazio!" }

        val creds = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

        return S3Client.builder()
            .region(Region.of(awsProperties.region))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .build()
    }

    @Bean
    fun awsProperties(): AwsProperties = awsProperties
}
