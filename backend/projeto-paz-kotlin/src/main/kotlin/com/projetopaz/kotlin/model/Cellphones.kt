package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "cellphones")
data class Cellphones(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var countryNumber: String,
    var ddd1: String,
    var ddd2: String?,
    var cellphone1: String,
    var cellphone2: String?,
    var createdAt: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
)