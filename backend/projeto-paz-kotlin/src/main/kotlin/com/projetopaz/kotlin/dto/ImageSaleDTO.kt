package com.projetopaz.kotlin.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ImageSaleDTO(
    @JsonIgnore
    val id: Long? = null,

    // será preenchido pelo backend após upload
    @JsonIgnore
    val url: String? = null,

    val alt: String, // obrigatório no envio
    val base64: String = "", // obrigatório no envio

    @JsonIgnore
    val status: Boolean = true
)
