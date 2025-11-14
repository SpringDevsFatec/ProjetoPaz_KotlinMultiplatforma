package com.projetopaz.kotlin.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "item_orders")
 class ItemOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    var order: Order? = null,   // ← MUDAR PARA var

    var productId: Long,
    var quantity: Int,
    var unitPrice: Double,
    var status: Boolean = true
)

