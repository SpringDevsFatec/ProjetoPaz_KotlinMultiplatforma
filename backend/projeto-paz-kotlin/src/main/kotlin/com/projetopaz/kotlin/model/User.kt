package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length=100, nullable = false)
    var name: String,
    @Column(length=100, nullable = true)
    var surname: String,
    @Column(length=100, nullable = true)
    var birthday: LocalDate,

    var urlImage: String? = null,
    @Column(length=100, unique = true, nullable = false)
    var email: String,
    @Column(length=255, nullable = false)
    var password: String,

    var status: Boolean = true,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var adress: Adress? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cellphones: MutableList<Cellphones> = mutableListOf(),

    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now()
)


