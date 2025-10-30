package com.projetopaz.kotlin.dto

data class ProductImageDTOView (
    var id: Long?,

    val url: String,

    val isFavorite: Boolean

) {
    constructor():this(
        id=0,
        url="",
        isFavorite = false
    )
}