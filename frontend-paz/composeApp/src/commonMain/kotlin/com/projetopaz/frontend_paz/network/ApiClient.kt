package com.projetopaz.frontend_paz.network

import com.projetopaz.frontend_paz.getPlatform
import com.projetopaz.frontend_paz.Platform
import com.projetopaz.frontend_paz.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    private val baseUrl = when (getPlatform()) {
        Platform.Android -> "http://10.0.2.2:8081"
        else -> "http://localhost:8081"
    }

    var currentUser: UserResponse? = null
        private set

    private var authToken: String? = null

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
                encodeDefaults = true
            })
        }
        install(DefaultRequest) {
            authToken?.let { token ->
                header("Authorization", "Bearer $token")
            }
            contentType(ContentType.Application.Json)
        }
    }

    // --- AUTH ---
    fun setToken(token: String) { authToken = token }

    fun clearToken() {
        authToken = null
        currentUser = null
    }

    suspend fun login(loginData: UserLogin): LoginResponse? {
        return try {
            val response = client.post("$baseUrl/api/auth/login") { setBody(loginData) }
            if (response.status.isSuccess()) {
                val data = response.body<LoginResponse>()
                setToken(data.token)
                currentUser = data.user
                data
            } else null
        } catch (e: Exception) {
            println("Login Error: ${e.message}")
            null
        }
    }

    suspend fun register(user: UserCreateRequest): Boolean {
        return try {
            val response = client.post("$baseUrl/api/auth/register") { setBody(user) }
            response.status.isSuccess()
        } catch (e: Exception) { false }
    }

    suspend fun updateUser(id: Long, req: UserCreateRequest): Boolean {
        return try {
            val response = client.put("$baseUrl/api/user") {
                parameter("id", id)
                setBody(req)
            }
            if (response.status.isSuccess()) {
                currentUser = response.body<UserResponse>()
                true
            } else false
        } catch (e: Exception) { false }
    }

    // --- PRODUTOS ---
    suspend fun getAllProducts(): List<Product> = try { client.get("$baseUrl/api/product").body() } catch (e: Exception) { emptyList() }
    suspend fun createProduct(req: ProductRequest) = try { client.post("$baseUrl/api/product") { setBody(req) }.status.isSuccess() } catch (e: Exception) { false }
    suspend fun updateProduct(id: Long, req: ProductRequest) = try { client.put("$baseUrl/api/product/$id") { setBody(req) }.status.isSuccess() } catch (e: Exception) { false }
    suspend fun deleteProduct(id: Long) = try { client.delete("$baseUrl/api/product/$id").status.isSuccess() } catch (e: Exception) { false }

    // --- CATEGORIAS ---
    suspend fun getAllCategories(): List<Category> = try { client.get("$baseUrl/api/category").body() } catch (e: Exception) { emptyList() }

    suspend fun createCategory(category: Category): Boolean {
        return try {
            client.post("$baseUrl/api/category") { setBody(category) }.status.isSuccess()
        } catch (e: Exception) { false }
    }

    suspend fun updateCategory(id: Long, category: Category): Boolean {
        return try {
            client.put("$baseUrl/api/category/$id") { setBody(category) }.status.isSuccess()
        } catch (e: Exception) { false }
    }

    suspend fun deleteCategory(id: Long): Boolean {
        return try {
            client.delete("$baseUrl/api/category/$id").status.isSuccess()
        } catch (e: Exception) { false }
    }

    // --- FORNECEDORES ---
    suspend fun getAllSuppliers(): List<Supplier> = try { client.get("$baseUrl/api/supplier").body() } catch (e: Exception) { emptyList() }

    suspend fun createSupplier(supplier: Supplier): Boolean {
        return try {
            client.post("$baseUrl/api/supplier") { setBody(supplier) }.status.isSuccess()
        } catch (e: Exception) { false }
    }

    suspend fun updateSupplier(id: Long, supplier: Supplier): Boolean {
        return try {
            client.put("$baseUrl/api/supplier/$id") { setBody(supplier) }.status.isSuccess()
        } catch (e: Exception) { false }
    }

    suspend fun deleteSupplier(id: Long): Boolean {
        return try {
            client.delete("$baseUrl/api/supplier/$id").status.isSuccess()
        } catch (e: Exception) { false }
    }

    // --- COMUNIDADES ---
    suspend fun getAllCommunities(): List<Community> = try { client.get("$baseUrl/api/communities").body() } catch (e: Exception) { emptyList() }
    suspend fun createCommunity(req: CommunityRequest) = try { client.post("$baseUrl/api/communities") { setBody(req) }.status.isSuccess() } catch (e: Exception) { false }
    suspend fun updateCommunity(id: Long, req: CommunityRequest) = try { client.put("$baseUrl/api/communities/$id") { setBody(req) }.status.isSuccess() } catch (e: Exception) { false }
    suspend fun deleteCommunity(id: Long) = try { client.delete("$baseUrl/api/communities/$id").status.isSuccess() } catch (e: Exception) { false }

    // --- VENDAS ---
    suspend fun getAllSales(): List<SaleResponse> = try { client.get("$baseUrl/api/sale").body() } catch (e: Exception) { emptyList() }
    suspend fun createSale(req: SaleRequest): SaleResponse? = try { client.post("$baseUrl/api/sale") { setBody(req) }.body() } catch (e: Exception) { null }
    suspend fun createOrder(saleId: Long, req: OrderRequest) = try { client.post("$baseUrl/api/order/$saleId") { setBody(req) }.status.isSuccess() } catch (e: Exception) { false }

    // --- SENSOR (IOT) ---
    // Atenção: A classe SensorData deve estar definida em Models.kt com @Serializable
    suspend fun getSensorData(): SensorData? = try { client.get("$baseUrl/api/sensor/current").body() } catch (e: Exception) { null }
}