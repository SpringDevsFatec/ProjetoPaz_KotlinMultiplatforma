package com.projetopaz.kotlin.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class SupplierCellphone (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    var countryNumber : String,

    var ddd1 : String,

    var ddd2 : String?,

    var cellphone1 : String,

    var cellphone2 : String?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt : LocalDateTime,

    @JsonBackReference
    @OneToOne(mappedBy="phone")
    val supplier : Supplier? = null
) {
    constructor():this(null, countryNumber="", ddd1="", ddd2=null,
        cellphone1="", cellphone2=null, createdAt=LocalDateTime.now()
    )
}