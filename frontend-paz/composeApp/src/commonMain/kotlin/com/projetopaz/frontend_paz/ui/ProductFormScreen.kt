package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    productToEdit: Product? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var price by remember { mutableStateOf(productToEdit?.price?.toString() ?: "") }
    var sku by remember { mutableStateOf(productToEdit?.sku ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var suppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var supplierExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        categories = ApiClient.getAllCategories()
        suppliers = ApiClient.getAllSuppliers()

        if (productToEdit != null) {
            selectedCategory = categories.find { it.id == productToEdit.category.id }
            productToEdit.supplier?.let { sup ->
                selectedSupplier = suppliers.find { it.id == sup.id }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (productToEdit != null) "Editar" else "Cadastrar", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Placeholder de Imagem (Backend não tem suporte ainda)
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFFAFAFA)).border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, null, tint = Color.Gray)
                    Text("(Backend sem suporte a img)", color = Color.Red, fontSize = 10.sp)
                }
            }

            PazInput("Nome *", name, { name = it })
            PazInput("Preço *", price, { price = it }, isNumber = true)
            PazInput("SKU *", sku, { sku = it })
            PazInput("Descrição", description, { description = it })

            // Dropdown Categoria
            Text("Categoria *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = it }) {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "Selecione...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFFAFAFA))
                )
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name ?: "") },
                            onClick = { selectedCategory = cat; categoryExpanded = false }
                        )
                    }
                }
            }

            // Dropdown Fornecedor
            Text("Fornecedor", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            ExposedDropdownMenuBox(expanded = supplierExpanded, onExpandedChange = { supplierExpanded = it }) {
                OutlinedTextField(
                    value = selectedSupplier?.name ?: "Selecione...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = supplierExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFFAFAFA))
                )
                ExposedDropdownMenu(expanded = supplierExpanded, onDismissRequest = { supplierExpanded = false }) {
                    suppliers.forEach { sup ->
                        DropdownMenuItem(
                            text = { Text(sup.name ?: "") },
                            onClick = { selectedSupplier = sup; supplierExpanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (selectedCategory != null && name.isNotBlank()) {
                        coroutineScope.launch {
                            val request = ProductRequest(
                                name = name, description = description, sku = sku,
                                price = price.toDoubleOrNull() ?: 0.0,
                                category = selectedCategory!!, // Envia Objeto Completo
                                supplier = selectedSupplier        // Envia Objeto Completo (ou null)
                            )
                            val success = if (productToEdit == null) ApiClient.createProduct(request) else ApiClient.updateProduct(productToEdit.id, request)
                            if (success) onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(8.dp)
            ) { Text("SALVAR", fontWeight = FontWeight.Bold) }
        }
    }
}