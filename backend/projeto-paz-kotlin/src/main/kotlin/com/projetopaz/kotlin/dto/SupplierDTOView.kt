package com.projetopaz.kotlin.dto

data class SupplierDTOView(
    var id : Long? = null,

    val name : String,
) {
    constructor() : this(id=0, name="")
}
