package com.projetopaz.kotlin.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "supplier")
data class Supplier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    var cnpj: String? = null,
    var type: String? = null,
    var occupation: String? = null,
    var observation: String? = null,

    @OneToMany(mappedBy = "supplier", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var addresses: MutableList<SupplierAddress> = mutableListOf(),

    @OneToMany(mappedBy = "supplier", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var cellphones: MutableList<CellphoneSupplier> = mutableListOf(),

    @Column(nullable = false)
    var status: Int = 1, // 1 = ativo, 0 = inativo

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: Instant? = null,

    @UpdateTimestamp
    var updatedAt: Instant? = null
)
