package com.projetopaz.frontend_paz.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Data classes para enviar dados para a API
@Serializable
data class CategoryRef(val id: Long)

@Serializable
data class SupplierRef(val id: Long)

@Serializable
data class ProductRequest(
    val name: String,
    val description: String?,
    val sku: String,
    val price: Double,
    val active: Boolean = true,
    val category: CategoryRef,
    val supplier: SupplierRef? // Fornecedor pode ser nulo
)

// Data classes para receber dados da API
@Serializable
data class Category(
    val id: Long,
    val name: String?,
    val description: String?,
    val active: Boolean
)

@Serializable
data class Supplier(
    val id: Long,
    val name: String?,
    val contactName: String?,
    val phone: String?,
    val email: String?
)

@Serializable
data class Product(
    val id: Long,
    val name: String?,
    val description: String?,
    val sku: String?,
    val price: Double,
    val active: Boolean,
    val category: Category,
    val supplier: Supplier? // Fornecedor pode ser nulo
)

object ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun getAllCategories(): List<Category> {
        return try {
            client.get("http://localhost:8081/api/category").body()
        } catch (e: Exception) {
            println("Erro ao buscar categorias: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllSuppliers(): List<Supplier> {
        return try {
            client.get("http://localhost:8081/api/supplier").body()
        } catch (e: Exception) {
            println("Erro ao buscar fornecedores: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllProducts(): List<Product> {
        return try {
            client.get("http://localhost:8081/api/product").body()
        } catch (e: Exception) {
            println("Erro ao buscar produtos: ${e.message}")
            emptyList()
        }
    }

    suspend fun createProduct(productRequest: ProductRequest): Boolean {
        return try {
            val response: HttpResponse = client.post("http://localhost:8081/api/product") {
                contentType(ContentType.Application.Json)
                setBody(productRequest)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Erro ao criar produto: ${e.message}")
            false
        }
    }

    suspend fun updateProduct(productId: Long, productRequest: ProductRequest): Boolean {
        return try {
            val response: HttpResponse = client.put("http://localhost:8081/api/product/$productId") {
                contentType(ContentType.Application.Json)
                setBody(productRequest)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Erro ao atualizar produto: ${e.message}")
            false
        }
    }

    suspend fun deleteProduct(productId: Long): Boolean {
        return try {
            val response: HttpResponse = client.delete("http://localhost:8081/api/product/$productId")
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Erro ao deletar produto: ${e.message}")
            false
        }
    }
}