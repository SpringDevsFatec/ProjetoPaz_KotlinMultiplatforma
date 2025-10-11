package com.projetopaz.frontend_paz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.projetopaz.frontend_paz.network.*
import kotlinx.coroutines.launch

enum class Screen { ProductList, ProductForm }

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.ProductList) }
        var refreshTrigger by remember { mutableStateOf(0) }
        var products by remember { mutableStateOf<List<Product>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        var productToEdit by remember { mutableStateOf<Product?>(null) }

        LaunchedEffect(refreshTrigger) {
            isLoading = true
            coroutineScope.launch {
                products = ApiClient.getAllProducts()
                isLoading = false
            }
        }

        when (currentScreen) {
            Screen.ProductList -> {
                ProductListScreen(
                    products = products,
                    isLoading = isLoading,
                    onAddProductClick = {
                        productToEdit = null
                        currentScreen = Screen.ProductForm
                    },
                    onDeleteProduct = { productId ->
                        coroutineScope.launch {
                            if (ApiClient.deleteProduct(productId)) {
                                refreshTrigger++
                            }
                        }
                    },
                    onEditProduct = { product ->
                        productToEdit = product
                        currentScreen = Screen.ProductForm
                    }
                )
            }
            Screen.ProductForm -> {
                ProductFormScreen(
                    product = productToEdit,
                    onProductSaved = {
                        refreshTrigger++
                        currentScreen = Screen.ProductList
                    },
                    onBackClick = { currentScreen = Screen.ProductList }
                )
            }
        }
    }
}

@Composable
fun ProductListScreen(
    products: List<Product>,
    isLoading: Boolean,
    onAddProductClick: () -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onEditProduct: (Product) -> Unit
) {
    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = onAddProductClick) { Icon(Icons.Default.Add, "Adicionar") } }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Seus Produtos", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(products) { product ->
                            ProductItem(
                                product = product,
                                onDeleteClick = { onDeleteProduct(product.id) },
                                onEditClick = { onEditProduct(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onDeleteClick: () -> Unit, onEditClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name ?: "", style = MaterialTheme.typography.titleMedium)
                Text(text = "Preço: R$ ${product.price}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Categoria: ${product.category.name ?: ""}", style = MaterialTheme.typography.bodySmall)
                product.supplier?.let {
                    Text(text = "Fornecedor: ${it.name ?: ""}", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Produto")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar Produto")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(product: Product?, onProductSaved: () -> Unit, onBackClick: () -> Unit) {
    val isEditing = product != null

    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var sku by remember { mutableStateOf(product?.sku ?: "") }
    val coroutineScope = rememberCoroutineScope()

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf(product?.category) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    var suppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }
    var selectedSupplier by remember { mutableStateOf(product?.supplier) }
    var isSupplierDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            categories = ApiClient.getAllCategories()
            suppliers = ApiClient.getAllSuppliers()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (isEditing) "Editar Produto" else "Cadastro de Produto", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome do Produto") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Preço") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU (Código)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = isCategoryDropdownExpanded, onExpandedChange = { isCategoryDropdownExpanded = it }) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Selecione uma Categoria",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = isCategoryDropdownExpanded, onDismissRequest = { isCategoryDropdownExpanded = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(text = { Text(category.name ?: "") }, onClick = {
                        selectedCategory = category
                        isCategoryDropdownExpanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = isSupplierDropdownExpanded, onExpandedChange = { isSupplierDropdownExpanded = it }) {
            OutlinedTextField(
                value = selectedSupplier?.name ?: "Selecione um Fornecedor (Opcional)",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSupplierDropdownExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = isSupplierDropdownExpanded, onDismissRequest = { isSupplierDropdownExpanded = false }) {
                suppliers.forEach { supplier ->
                    DropdownMenuItem(text = { Text(supplier.name ?: "") }, onClick = {
                        selectedSupplier = supplier
                        isSupplierDropdownExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBackClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Text("Voltar")
            }
            Button(
                onClick = {
                    selectedCategory?.let { cat ->
                        coroutineScope.launch {
                            val productRequest = ProductRequest(
                                name = name,
                                price = price.toDoubleOrNull() ?: 0.0,
                                sku = sku,
                                description = product?.description ?: "Adicionado pelo App",
                                category = CategoryRef(id = cat.id),
                                supplier = selectedSupplier?.let { sup -> SupplierRef(id = sup.id) }
                            )

                            val success = if (isEditing) {
                                ApiClient.updateProduct(product!!.id, productRequest)
                            } else {
                                ApiClient.createProduct(productRequest)
                            }

                            if (success) {
                                onProductSaved()
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedCategory != null
            ) {
                Text(if (isEditing) "Salvar Alterações" else "Salvar Produto")
            }
        }
    }
}