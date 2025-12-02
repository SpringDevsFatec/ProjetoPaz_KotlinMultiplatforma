package com.projetopaz.frontend_paz.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
// --- PRODUTO ---
@Serializable
data class Product(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val sku: String? = null,

    // CORREÇÃO 1: Nome igual ao do Backend
    val salePrice: Double = 0.0,
    val costPrice: Double = 0.0,

    val active: Boolean = true,
    val donation: Boolean = false,

    // CORREÇÃO 2: Agora é uma lista (Backend manda lista)
    val categories: List<Category> = emptyList(),

    val supplier: Supplier? = null,
    val stock: Stock? = null,
    val images: List<ImageProduct> = emptyList()
)

@Serializable
data class ProductRequest(
    val name: String,
    val description: String?,
    val costPrice: Double,
    val salePrice: Double,
    val isFavorite: Boolean = false,
    val donation: Boolean = false,
    val supplierId: Long?,
    val stock: Stock?,
    val categoryIds: List<Long> = emptyList()
)

// --- ESTOQUE (Ajustado para os nomes do Banco) ---
@Serializable
data class Stock(
    val id: Long = 0,

    // O Backend provavelmente usa 'qtd' (igual ao banco)
    @SerialName("qtd")
    val quantity: Int = 0,

    val minQuantity: Int = 0,

    // O Backend provavelmente usa 'fabrication'
    @SerialName("fabrication")
    val manufactureDate: String? = null,

    // O Backend provavelmente usa 'maturity'
    @SerialName("maturity")
    val expirationDate: String? = null
)

@Serializable
data class ImageProduct(
    val id: Long = 0,
    val url: String = ""
)

@Serializable
data class ImageBatchDTO(
    val images: List<ImageDTOItem>
)

@Serializable
data class ImageDTOItem(
    val base64: String
)

@Serializable
data class Category(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val categoryType: String = "PRODUTO", // Valor padrão
    val favorite: Boolean = false,
    val imgBase64: String? = null, // Campo para enviar a imagem nova
    val imgCategory: String? = null // Campo para receber a URL da imagem existente
)

@Serializable
data class Supplier(
    val id: Long = 0,
    val name: String? = null,
    val cnpj: String? = null,
    val type: String? = null,       // Tipo de Fornecedor
    val occupation: String? = null, // Vamos usar para Razão Social
    val observation: String? = null,
    val addresses: List<SupplierAddress> = emptyList(),
    val cellphones: List<CellphoneSupplier> = emptyList()
)

@Serializable
data class SupplierAddress(
    val id: Long = 0,
    val street: String = "",
    val number: String = "",
    val complement: String? = null,
    val neighborhood: String = "",
    val cep: String = "",
    val city: String = "",
    val state: String = ""
)

@Serializable
data class CellphoneSupplier(
    val id: Long = 0,
    val countryNumber: String = "",
    val ddd1: String = "",
    val ddd2: String = "",
    val phone1: String = "",
    val phone2: String = ""
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