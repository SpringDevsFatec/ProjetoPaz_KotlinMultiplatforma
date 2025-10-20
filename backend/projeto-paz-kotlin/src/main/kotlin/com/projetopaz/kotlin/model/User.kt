package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,
    var surname: String,
    var birthday: LocalDate,
    var urlImage: String? = null,
    var email: String,
    var password: String,
    var status: Boolean = true,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var adress: Adress? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cellphones: MutableList<Cellphones> = mutableListOf(),

    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now()
)