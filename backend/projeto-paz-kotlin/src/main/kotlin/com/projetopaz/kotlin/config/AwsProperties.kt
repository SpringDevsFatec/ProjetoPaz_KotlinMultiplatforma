package com.projetopaz.kotlin.config

data class AwsProperties(
    val region: String,
    val accessKey: String,
    val secretKey: String,
    val bucketUser: String,
    val bucketProduct: String,
    val bucketSale: String
)
