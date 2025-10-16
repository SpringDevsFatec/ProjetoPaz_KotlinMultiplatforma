package com.projetopaz.frontend_paz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.network.Category
import com.projetopaz.frontend_paz.network.Product
import com.projetopaz.frontend_paz.network.Supplier
import kotlinx.coroutines.launch

// Enum expandido para controlar todas as nossas telas
enum class Screen {
    MainHub,
    ProductList, ProductForm,
    CategoryList, CategoryForm,
    SupplierList, SupplierForm
}

@Composable
fun App() {
    AppTheme {
        var currentScreen by remember { mutableStateOf(Screen.MainHub) }

        // Estados para guardar o item que será editado
        var productToEdit by remember { mutableStateOf<Product?>(null) }
        var categoryToEdit by remember { mutableStateOf<Category?>(null) }
        var supplierToEdit by remember { mutableStateOf<Supplier?>(null) }

        // Estados de dados e carregamento para a lista de produtos
        var products by remember { mutableStateOf<List<Product>>(emptyList()) }
        var isLoadingProducts by remember { mutableStateOf(true) }
        var productRefreshTrigger by remember { mutableStateOf(0) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(productRefreshTrigger) {
            isLoadingProducts = true
            coroutineScope.launch {
                products = ApiClient.getAllProducts()
                isLoadingProducts = false
            }
        }

        // Lógica de navegação principal
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (currentScreen) {
                Screen.MainHub -> MainHub(
                    onNavigateToProducts = { currentScreen = Screen.ProductList },
                    onNavigateToCategories = { currentScreen = Screen.CategoryList },
                    onNavigateToSuppliers = { currentScreen = Screen.SupplierList }
                )

                // Roteamento para as telas de Produto (CORRIGIDO)
                Screen.ProductList -> ProductListScreen(
                    initialProducts = products,
                    isLoading = isLoadingProducts,
                    onAddProductClick = {
                        productToEdit = null
                        currentScreen = Screen.ProductForm
                    },
                    onDeleteProduct = { productId ->
                        coroutineScope.launch {
                            if (ApiClient.deleteProduct(productId)) {
                                productRefreshTrigger++
                            }
                        }
                    },
                    onEditProduct = { product ->
                        productToEdit = product
                        currentScreen = Screen.ProductForm
                    },
                    onBackClick = { currentScreen = Screen.MainHub }
                )
                Screen.ProductForm -> ProductFormScreen(
                    product = productToEdit,
                    onProductSaved = { // Nome do parâmetro corrigido
                        productRefreshTrigger++
                        currentScreen = Screen.ProductList
                    },
                    onBackClick = { currentScreen = Screen.ProductList }
                )

                // Roteamento para as telas de Categoria (CORRIGIDO)
                Screen.CategoryList -> CategoryListScreen(
                    onAddClick = {
                        categoryToEdit = null
                        currentScreen = Screen.CategoryForm
                    },
                    onEditClick = { category ->
                        categoryToEdit = category
                        currentScreen = Screen.CategoryForm
                    },
                    onBackClick = { currentScreen = Screen.MainHub }
                )
                Screen.CategoryForm -> CategoryFormScreen(
                    category = categoryToEdit,
                    onSave = { currentScreen = Screen.CategoryList },
                    onBackClick = { currentScreen = Screen.CategoryList }
                )

                // Roteamento para as telas de Fornecedor (CORRIGIDO)
                Screen.SupplierList -> SupplierListScreen(
                    onAddClick = {
                        supplierToEdit = null
                        currentScreen = Screen.SupplierForm
                    },
                    onEditClick = { supplier ->
                        supplierToEdit = supplier
                        currentScreen = Screen.SupplierForm
                    },
                    onBackClick = { currentScreen = Screen.MainHub }
                )
                Screen.SupplierForm -> SupplierFormScreen(
                    supplier = supplierToEdit,
                    onSave = { currentScreen = Screen.SupplierList },
                    onBackClick = { currentScreen = Screen.SupplierList }
                )
            }
        }
    }
}

// Tela principal que serve como menu de navegação
@Composable
fun MainHub(
    onNavigateToProducts: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSuppliers: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onNavigateToProducts, modifier = Modifier.fillMaxWidth()) {
            Text("Gerenciar Produtos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToCategories, modifier = Modifier.fillMaxWidth()) {
            Text("Gerenciar Categorias")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToSuppliers, modifier = Modifier.fillMaxWidth()) {
            Text("Gerenciar Fornecedores")
        }
    }
}

