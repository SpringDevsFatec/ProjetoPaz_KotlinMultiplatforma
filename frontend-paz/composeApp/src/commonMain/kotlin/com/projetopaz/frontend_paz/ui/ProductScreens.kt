package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// --- TELA DE LISTAGEM DE PRODUTOS COM FILTRO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    key: Int,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit
) {
    var allProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var filteredProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Estados do Filtro
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterName by remember { mutableStateOf("") }
    var filterMinPrice by remember { mutableStateOf("") }
    var filterMaxPrice by remember { mutableStateOf("") }
    var availableCategories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var filterCategoryId by remember { mutableStateOf<Long?>(null) }

    // Carrega dados iniciais
    LaunchedEffect(key) {
        allProducts = ApiClient.getAllProducts()
        filteredProducts = allProducts
        availableCategories = ApiClient.getAllCategories()
    }

    // Função de Filtragem Local
    fun applyFilters() {
        filteredProducts = allProducts.filter { p ->
            val matchName = p.name.contains(filterName, ignoreCase = true)
            val price = p.salePrice
            val min = filterMinPrice.toDoubleOrNull() ?: 0.0
            val max = filterMaxPrice.toDoubleOrNull() ?: Double.MAX_VALUE
            val matchPrice = price in min..max
            // Verifica se o produto tem a categoria selecionada (se houver filtro)
            val matchCategory = if (filterCategoryId == null) true else p.categories.any { it.id == filterCategoryId }

            matchName && matchPrice && matchCategory
        }
        showFilterSheet = false
    }

    // Limpar Filtros
    fun clearFilters() {
        filterName = ""
        filterMinPrice = ""
        filterMaxPrice = ""
        filterCategoryId = null
        filteredProducts = allProducts
        showFilterSheet = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Produtos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                actions = {
                    // BOTÃO DE FILTRO
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.FilterList, "Filtrar", tint = if(filterName.isNotEmpty() || filterCategoryId != null) Color(0xFF4CAF50) else PazBlack)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = PazBlack, contentColor = PazWhite) {
                Icon(Icons.Default.Add, "Adicionar")
            }
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mostra quantos resultados achou
            item {
                Text("${filteredProducts.size} produtos encontrados", fontSize = 12.sp, color = Color.Gray)
            }

            items(filteredProducts) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onEditClick(product) },
                    colors = CardDefaults.cardColors(containerColor = PazWhite),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (product.images.isNotEmpty()) {
                            AsyncImage(
                                model = product.images[0].url,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                        } else {
                            Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Inventory2, null, tint = PazWhite)
                            }
                            Spacer(Modifier.width(16.dp))
                        }

                        Column {
                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("R$ ${product.salePrice}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            val stock = product.stock?.quantity ?: 0
                            Text("Estoque: $stock", fontSize = 12.sp, color = if (stock > 0) Color.Gray else Color.Red)
                        }
                    }
                }
            }
        }
    }

    // --- POP-UP DE FILTRO (ModalBottomSheet) ---
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = PazWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 32.dp), // Espaço para navegação
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Filtrar Produtos", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                PazInput("Nome do Produto", filterName, { filterName = it })

                Text("Faixa de Preço", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) { PazInput("Min R$", filterMinPrice, { filterMinPrice = it }) }
                    Box(Modifier.weight(1f)) { PazInput("Max R$", filterMaxPrice, { filterMaxPrice = it }) }
                }

                Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                // Chips de categoria simples (Agora com scroll horizontal funcionando)
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    availableCategories.forEach { cat ->
                        FilterChip(
                            selected = filterCategoryId == cat.id,
                            onClick = { filterCategoryId = if (filterCategoryId == cat.id) null else cat.id },
                            label = { Text(cat.name) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { clearFilters() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Limpar", color = PazBlack)
                    }
                    Button(
                        onClick = { applyFilters() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    }
}

// --- TELA DE FORMULÁRIO ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun ProductFormScreen(
    productToEdit: Product? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var salePrice by remember { mutableStateOf(productToEdit?.salePrice?.toString() ?: "") }
    var costPrice by remember { mutableStateOf(productToEdit?.costPrice?.toString() ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var isDonation by remember { mutableStateOf(productToEdit?.donation ?: false) }

    var availableCategories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var availableSuppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }

    var selectedCategoryIds by remember { mutableStateOf(productToEdit?.categories?.map { it.id }?.toSet() ?: emptySet()) }
    var selectedSupplierId by remember { mutableStateOf<Long?>(productToEdit?.supplier?.id) }

    var expandedCat by remember { mutableStateOf(false) }
    var expandedSup by remember { mutableStateOf(false) }

    var quantity by remember { mutableStateOf(productToEdit?.stock?.quantity?.toString() ?: "") }
    var manufactureDate by remember { mutableStateOf(formatBackendDateToUi(productToEdit?.stock?.manufactureDate)) }
    var expirationDate by remember { mutableStateOf(formatBackendDateToUi(productToEdit?.stock?.expirationDate)) }

    var imgBase64 by remember { mutableStateOf<String?>(null) }
    var hasNewImage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        availableCategories = ApiClient.getAllCategories()
        availableSuppliers = ApiClient.getAllSuppliers()
    }

    val launcher = rememberFilePickerLauncher(type = PickerType.Image, mode = PickerMode.Single, title = "Foto do Produto") { file ->
        if (file != null) {
            coroutineScope.launch {
                val bytes = file.readBytes()
                imgBase64 = Base64.encode(bytes)
                hasNewImage = true
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (productToEdit != null) "Editar Produto" else "Cadastro de Produto", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(scrollState).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PazInput("Nome", name, { name = it })

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { PazInput("Preço Venda", salePrice, { salePrice = it }) }
                Box(Modifier.weight(1f)) { PazInput("Custo", costPrice, { costPrice = it }) }
            }

            Text("Tipo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = !isDonation, onClick = { isDonation = false })
                Text("Compra")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = isDonation, onClick = { isDonation = true })
                Text("Doação")
            }

            Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = if (selectedCategoryIds.isEmpty()) "Selecione..." else "${selectedCategoryIds.size} selecionada(s)",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { IconButton(onClick = { expandedCat = !expandedCat }) { Icon(Icons.Default.ArrowDropDown, null) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PazWhite, unfocusedContainerColor = PazWhite)
                )
                DropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                    availableCategories.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = selectedCategoryIds.contains(cat.id), onCheckedChange = null)
                                    Text(cat.name)
                                }
                            },
                            onClick = {
                                val current = selectedCategoryIds.toMutableSet()
                                if (current.contains(cat.id)) current.remove(cat.id) else current.add(cat.id)
                                selectedCategoryIds = current
                            }
                        )
                    }
                }
            }

            Text("Fornecedor", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = availableSuppliers.find { it.id == selectedSupplierId }?.name ?: "Selecione...",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { IconButton(onClick = { expandedSup = !expandedSup }) { Icon(Icons.Default.ArrowDropDown, null) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PazWhite, unfocusedContainerColor = PazWhite)
                )
                DropdownMenu(expanded = expandedSup, onDismissRequest = { expandedSup = false }) {
                    availableSuppliers.forEach { sup ->
                        DropdownMenuItem(text = { Text(sup.name ?: "Sem Nome") }, onClick = { selectedSupplierId = sup.id; expandedSup = false })
                    }
                }
            }

            Text("Imagem", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))
                    .background(PazWhite).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .clickable { launcher.launch() },
                contentAlignment = Alignment.Center
            ) {
                if (hasNewImage) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp))
                        Text("Imagem Selecionada!", color = Color(0xFF4CAF50))
                    }
                } else if (productToEdit?.images?.isNotEmpty() == true) {
                    AsyncImage(model = productToEdit.images[0].url, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                        Text("Toque para adicionar", color = Color.Gray)
                    }
                }
            }

            PazInput("Descrição", description, { description = it })

            Text("Para fins de Estoque", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            Card(colors = CardDefaults.cardColors(containerColor = PazWhite)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PazInput("Quantidade", quantity, { quantity = it })
                    PazInput("Data de Fabricação", manufactureDate, { manufactureDate = it }, visualTransformation = DateTransformation())
                    PazInput("Data de Vencimento", expirationDate, { expirationDate = it }, visualTransformation = DateTransformation())
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val stockDto = Stock(
                                quantity = quantity.toIntOrNull() ?: 0,
                                manufactureDate = convertDateToBackendFormat(manufactureDate),
                                expirationDate = convertDateToBackendFormat(expirationDate)
                            )

                            val request = ProductRequest(
                                name = name, description = description, costPrice = costPrice.toDoubleOrNull() ?: 0.0,
                                salePrice = salePrice.toDoubleOrNull() ?: 0.0, isFavorite = false, donation = isDonation,
                                supplierId = selectedSupplierId, categoryIds = selectedCategoryIds.toList(), stock = stockDto
                            )

                            val savedProduct = if (productToEdit == null) ApiClient.createProduct(request) else ApiClient.updateProduct(productToEdit.id, request)

                            if (savedProduct != null && imgBase64 != null) {
                                ApiClient.uploadProductImages(savedProduct.id, listOf(imgBase64!!))
                            }
                            if (savedProduct != null) onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (productToEdit != null) "SALVAR ALTERAÇÕES" else "CADASTRAR PRODUTO", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}