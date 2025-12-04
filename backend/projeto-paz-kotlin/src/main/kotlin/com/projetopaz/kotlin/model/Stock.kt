package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "stock")
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,

    @Column(name = "qtd")
    var qtd: Int = 0,

    var maturity: String? = null,   // pode ajustar tipo se precisar
    var fabrication: String? = null,

    @Column(nullable = false)
    var status: Int = 1


)
