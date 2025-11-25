package com.projetopaz.kotlin.model

import jakarta.persistence.*

@Entity
@Table(name = "supplier_cellphone")
data class CellphoneSupplier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var countryNumber: String? = null,
    var ddd1: String? = null,
    var ddd2: String? = null,
    var phone1: String? = null,
    var phone2: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    var supplier: Supplier? = null,

    @Column(nullable = false)
    var status: Int = 1
)
