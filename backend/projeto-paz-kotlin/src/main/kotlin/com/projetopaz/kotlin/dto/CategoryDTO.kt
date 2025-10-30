package com.projetopaz.kotlin.dto

data class CategoryDTO(
    var id: Long?,
    val name: String,
    val type: String?,
    val imageUrl: String,
    val imageAltText: String?,
    val description: String?,
    val active: Boolean,
    val createUser: Long,
    val updateUser: Long?,
) {
    constructor():this(id=0, name="", type="",
        imageUrl="", imageAltText=null, description=null,
        active=true, createUser=0, updateUser=0
    )
}
