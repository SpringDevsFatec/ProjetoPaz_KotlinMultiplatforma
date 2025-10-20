package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "adress")
data class Adress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var street: String,
    var cep: String,
    var quarter: String,
    var number: String,
    var complement: String? = null,
    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
)