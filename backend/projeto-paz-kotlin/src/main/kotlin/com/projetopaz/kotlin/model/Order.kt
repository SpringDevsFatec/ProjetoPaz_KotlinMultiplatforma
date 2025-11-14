package com.projetopaz.kotlin.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDate

@Entity
@Table(name = "orders")
 class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @JsonBackReference
    var sale: Sale? = null,  // ← MUDAR PARA var

    var paymentMethod: String,
    var total_amount_order: Double? = null,
    var status: Boolean = true,
    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),

    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @BatchSize(size = 50)
    @JsonManagedReference
    var items: Set<ItemOrder> = HashSet()
)

