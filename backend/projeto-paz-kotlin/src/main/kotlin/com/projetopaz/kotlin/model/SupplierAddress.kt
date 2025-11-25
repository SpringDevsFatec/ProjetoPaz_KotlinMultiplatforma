package com.projetopaz.kotlin.model

import jakarta.persistence.*

@Entity
@Table(name = "supplier_address")
data class SupplierAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var street: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var neighborhood: String? = null,
    var cep: String? = null,
    var city: String? = null,
    var state: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    var supplier: Supplier? = null,

    @Column(nullable = false)
    var status: Int = 1
)
