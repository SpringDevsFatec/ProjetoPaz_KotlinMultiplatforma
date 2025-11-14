package com.projetopaz.kotlin.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDate

@Entity
@Table(name = "communities")
 class Community(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,
    var description: String,
    var cep: String,
    var quarter: String,
    var number: String,
    var complement: String? = null,
    var createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),
    var status: Boolean = true,

    @OneToMany(mappedBy = "community", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    @JsonManagedReference
    var sales: Set<Sale> = HashSet()
)
