package com.projetopaz.kotlin.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class SaleDTO(
    val id: Long? = null,
    val sellerId: Long? = null,
    val communityId: Long? = null,
    val isSelfService: Boolean,
    val observation: String? = null,
    val status: Int = 1,
    val createdAt: LocalDate? = null,
    var updatedAt: LocalDate? = null
)

