package com.projetopaz.kotlin.dto

data class ProductImageDTO(
    val id: Long?,
    val url: String,
    val altText: String?,
    val isFavorite: Boolean,
    //val product: Long?
) {
    constructor():this(id=0, url="", altText=null,
        isFavorite=false, //product=null
    )
}
