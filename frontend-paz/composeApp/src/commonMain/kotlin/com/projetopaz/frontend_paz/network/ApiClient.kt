package com.projetopaz.frontend_paz.network

import com.projetopaz.frontend_paz.Platform
import com.projetopaz.frontend_paz.getPlatform
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProductRequest(
    val name: String,
    val description: String?,
    val sku: String,
    val price: Double,
    val active: Boolean = true,
    val categoryId: Long,
    val supplierId: Long?
)

@Serializable
data class Category(
    val id: Long? = null,
    val name: String?,
    val description: String?,
    val active: Boolean
)

@Serializable
data class Supplier(
    val id: Long? = null,
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
    private val baseUrl = when (getPlatform()) {
        Platform.Android -> "http://10.0.2.2:8081" // IP especial para o Emulador Android
        else -> "http://localhost:8081"           // Funciona para Desktop e Web
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    // --- CATEGORIAS ---
    suspend fun getAllCategories(): List<Category> {
        return try {
            client.get("$baseUrl/api/category").body()
        } catch (e: Exception) {
            println("Erro ao buscar categorias: ${e.message}")
            emptyList()
        }
    }
    suspend fun createCategory(category: Category): Boolean {
        return try {
            val response: HttpResponse = client.post("$baseUrl/api/category") {
                contentType(ContentType.Application.Json)
                setBody(category)
            }
            response.status.isSuccess()
        } catch (e: Exception) { false }
    }
    suspend fun updateCategory(id: Long, category: Category): Boolean {
        return try {
            val response: HttpResponse = client.put("$baseUrl/api/category/$id") {
                contentType(ContentType.Application.Json)
                setBody(category)
            }
            response.status.isSuccess()
        } catch (e: Exception) { false }
    }
    suspend fun deleteCategory(id: Long): Boolean {
        return try {
            client.delete("$baseUrl/api/category/$id").status.isSuccess()
        } catch (e: Exception) { false }
    }

    // --- FORNECEDORES ---
    suspend fun getAllSuppliers(): List<Supplier> {
        return try {
            client.get("$baseUrl/api/supplier").body()
        } catch (e: Exception) {
            println("Erro ao buscar fornecedores: ${e.message}")
            emptyList()
        }
    }
    suspend fun createSupplier(supplier: Supplier): Boolean {
        return try {
            val response: HttpResponse = client.post("$baseUrl/api/supplier") {
                contentType(ContentType.Application.Json)
                setBody(supplier)
            }
            response.status.isSuccess()
        } catch (e: Exception) { false }
    }
    suspend fun updateSupplier(id: Long, supplier: Supplier): Boolean {
        return try {
            val response: HttpResponse = client.put("$baseUrl/supplier/$id") { // URL diferente conforme Padrões.txt
                contentType(ContentType.Application.Json)
                setBody(supplier)
            }
            response.status.isSuccess()
        } catch (e: Exception) { false }
    }
    suspend fun deleteSupplier(id: Long): Boolean {
        return try {
            client.delete("$baseUrl/supplier/$id").status.isSuccess() // URL diferente conforme Padrões.txt
        } catch (e: Exception) { false }
    }

    // --- PRODUTOS ---
    suspend fun getAllProducts(): List<Product> {
        return try {
            client.get("$baseUrl/api/product").body()
        } catch (e: Exception) {
            println("Erro ao buscar produtos: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchProducts(query: String): List<Product> {
        return try {
            if (query.isBlank()) {
                return getAllProducts()
            }
            client.get("$baseUrl/api/product/search?q=$query").body()
        } catch (e: Exception) {
            println("Erro ao buscar produtos: ${e.message}")
            emptyList()
        }
    }

    suspend fun createProduct(productRequest: ProductRequest): Boolean {
        return try {
            val response: HttpResponse = client.post("$baseUrl/api/product") {
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
            val response: HttpResponse = client.put("$baseUrl/api/product/$productId") {
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
            val response: HttpResponse = client.delete("$baseUrl/api/product/$productId")
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Erro ao deletar produto: ${e.message}")
            false
        }
    }
}