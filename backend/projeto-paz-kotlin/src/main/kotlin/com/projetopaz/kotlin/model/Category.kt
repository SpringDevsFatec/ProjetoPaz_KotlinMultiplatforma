package com.projetopaz.kotlin.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "category")
data class Category(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(name = "category_type", nullable = false)
    var categoryType: String = "",

    @Column(name = "img_category")
    var imgCategory: String? = null,

    @Column(name = "alt_image")
    var altImage: String? = null,

    @Column(name = "favorite")
    var favorite: Boolean = false,

    @Column(nullable = false)
    var status: Int = 1, // 1 = ativo / 0 = inativo

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "create_user")
    var createUser: Long? = null,

    @Column(name = "update_user")
    var updateUser: Long? = null
)
