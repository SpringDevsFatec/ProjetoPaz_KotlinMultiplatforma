package com.projetopaz.kotlin.dto

data class CategoryDTOView(
    var id: Long?,

    var name: String,
) {
    constructor() : this(
        id=0,
        name=""
    )
}
