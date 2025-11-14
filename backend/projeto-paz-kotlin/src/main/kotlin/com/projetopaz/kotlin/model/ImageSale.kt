package com.projetopaz.kotlin.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "image_sales")
 class ImageSale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var url: String,
    var alt: String,
    var status: Boolean = true,
    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @JsonBackReference
    var sale: Sale? = null
)
