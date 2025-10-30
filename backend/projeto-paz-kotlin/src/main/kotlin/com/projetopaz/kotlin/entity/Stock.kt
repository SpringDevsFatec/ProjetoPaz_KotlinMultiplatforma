package com.projetopaz.kotlin.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.Positive
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?=null,

    //@field:Positive(message = "A quantidade deve ser um valor positivo.")
    var quantity:Int,

    @JsonFormat(pattern = "yyyy-MM-dd")
    var fabrication:LocalDate?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    var maturity:LocalDate?,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime?,

    @JsonBackReference
    @OneToOne(mappedBy = "stock")
    val product: Product? = null

) {
    constructor():this(id=null, quantity=0,
        fabrication=null, maturity=null,
        createdAt=LocalDateTime.now(), updatedAt=null
    )
}
