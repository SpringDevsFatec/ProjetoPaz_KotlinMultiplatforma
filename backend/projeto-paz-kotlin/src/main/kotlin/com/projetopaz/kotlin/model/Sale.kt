package com.projetopaz.kotlin.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDate

@Entity
@Table(name = "sales")
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    var community: Community? = null,

    var sellerId: Long,
    var isSelfService: Boolean,
    var status: Int = 1,
    var observation: String? = null,
    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),

    @OneToMany(mappedBy = "sale", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    @JsonManagedReference
    val images: Set<ImageSale> = HashSet(),

    @OneToMany(mappedBy = "sale", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    @JsonManagedReference
    val orders: Set<Order> = HashSet()
)
