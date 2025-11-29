package com.projetopaz.frontend_paz.model

import kotlinx.serialization.Serializable

// --- AUTH & USUÁRIO ---
@Serializable
data class UserLogin(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val user: UserResponse)

@Serializable
data class UserResponse(
    val id: Long,
    val name: String,
    val surname: String?,
    val email: String,
    val urlImage: String?,
    val birthday: String?,
    val adress: Address?,
    val cellphones: List<Cellphone>?
)

@Serializable
data class UserCreateRequest(
    val name: String,
    val surname: String? = null,
    val birthday: String,
    val email: String,
    val password: String,
    val urlImage: String? = null,
    val adress: Address,
    val cellphones: List<Cellphone> = emptyList()
)

@Serializable
data class Address(
    val street: String = "",
    val cep: String = "",
    val quarter: String = "",
    val number: String = "",
    val complement: String? = null
)

@Serializable
data class Cellphone(
    val countryNumber: String = "+55",
    val ddd1: String,
    val ddd2: String? = null,
    val cellphone1: String,
    val cellphone2: String? = null
)

// --- COMUNIDADE ---
@Serializable
data class Community(
    val id: Long = 0,
    val name: String,
    val description: String,
    val cep: String,
    val quarter: String,
    val number: String,
    val complement: String?,
    val status: Boolean = true
)

@Serializable
data class CommunityRequest(
    val name: String,
    val description: String,
    val cep: String,
    val quarter: String,
    val number: String,
    val complement: String? = null
)

// --- PRODUTO, CATEGORIA, FORNECEDOR ---
@Serializable
data class Product(
    val id: Long,
    val name: String?,
    val description: String?,
    val sku: String?,
    val price: Double,
    val active: Boolean,
    val category: Category,
    val supplier: Supplier?
)

@Serializable
data class Category(
    val id: Long = 0,
    val name: String?,
    val description: String? = null
)

@Serializable
data class Supplier(
    val id: Long = 0,
    val name: String,
    val contactName: String? = null,
    val phone: String? = null,
    val email: String? = null
)

// Alterado para enviar OBJETO COMPLETO e evitar erro 400 no Backend
@Serializable
data class ProductRequest(
    val name: String,
    val description: String?,
    val sku: String,
    val price: Double,
    val category: Category, // Mudou de CategoryRef para Category
    val supplier: Supplier? // Mudou de SupplierRef para Supplier
)

// --- VENDAS & PEDIDOS ---
@Serializable
data class SaleResponse(
    val id: Long,
    val sellerId: Long,
    val isSelfService: Boolean,
    val observation: String?,
    val status: Int,
    val createdAt: String?,
    val community: Community?,
    val orders: List<OrderResponse>,
    val images: List<ImageSaleResponse>
)

@Serializable
data class SaleRequest(
    val communityId: Long,
    val isSelfService: Boolean,
    val observation: String? = null,
    val status: Int = 1
)

@Serializable
data class OrderResponse(
    val id: Long,
    val paymentMethod: String,
    val total: Double,
    val status: Int,
    val items: List<ItemOrder>
)

@Serializable
data class OrderRequest(
    val paymentMethod: String,
    val items: List<ItemOrderRequest>
)

@Serializable
data class ItemOrder(
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double
)

@Serializable
data class ItemOrderRequest(
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double
)

@Serializable
data class ImageSaleResponse(
    val id: Long?,
    val url: String,
    val alt: String
)

@Serializable
data class SensorData(
    val temperature: Float,
    val humidity: Float
)