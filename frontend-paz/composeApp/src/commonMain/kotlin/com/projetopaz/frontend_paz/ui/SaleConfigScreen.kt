package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import com.projetopaz.frontend_paz.model.Community
import com.projetopaz.frontend_paz.model.Product
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleConfigScreen(
    onBackClick: () -> Unit,
    onStartSale: (Long, Boolean) -> Unit // Retorna communityId e isAutoService
) {
    val coroutineScope = rememberCoroutineScope()

    // Dados da API
    var communities by remember { mutableStateOf<List<Community>>(emptyList()) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Estado do Formulário
    var selectedCommunity by remember { mutableStateOf<Community?>(null) }
    var isCommunityDropdownExpanded by remember { mutableStateOf(false) }
    var isAutoService by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Carrega dados ao abrir a tela
    LaunchedEffect(Unit) {
        communities = ApiClient.getAllCommunities()
        products = ApiClient.getAllProducts()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(24.dp))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    Text("25°🌡️", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazBlack, titleContentColor = PazWhite, navigationIconContentColor = PazWhite, actionIconContentColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Configurar Venda", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            // Switch Manual / Auto
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manual", fontWeight = if (!isAutoService) FontWeight.Bold else FontWeight.Normal)
                Switch(
                    checked = isAutoService,
                    onCheckedChange = { isAutoService = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = PazWhite, checkedTrackColor = PazBlack),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text("AutoAtendimento", fontWeight = if (isAutoService) FontWeight.Bold else FontWeight.Normal)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Busca de Produto (Visual apenas por enquanto)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar produto...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PazWhite, unfocusedContainerColor = PazWhite)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown de Comunidade
            Text("Comunidade", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))

            ExposedDropdownMenuBox(
                expanded = isCommunityDropdownExpanded,
                onExpandedChange = { isCommunityDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCommunity?.name ?: "Selecione a Comunidade",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCommunityDropdownExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PazWhite, unfocusedContainerColor = PazWhite)
                )
                ExposedDropdownMenu(
                    expanded = isCommunityDropdownExpanded,
                    onDismissRequest = { isCommunityDropdownExpanded = false }
                ) {
                    communities.forEach { community ->
                        DropdownMenuItem(
                            text = { Text(community.name) },
                            onClick = {
                                selectedCommunity = community
                                isCommunityDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Produtos "Destaque"
            Text("Produtos em Destaque", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Exibe apenas os 3 primeiros para não poluir a tela de config
                items(products.take(3)) { product ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.fillMaxWidth().border(1.dp, PazBlack, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Imagem com Coil (Já atualizado para ver a foto)
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(PazWhite, RoundedCornerShape(8.dp))
                                    .border(1.dp, PazBlack, RoundedCornerShape(8.dp))
                                    .clip(RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (product.images.isNotEmpty()) {
                                    AsyncImage(
                                        model = product.images[0].url,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(Icons.Default.Image, null, tint = Color.Gray)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nome: ${product.name}", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                // CORRIGIDO: price -> salePrice
                                Text("Preço: R$ ${product.salePrice}", fontSize = 12.sp)

                                // CORRIGIDO: category -> categories (Lista)
                                val catName = product.categories.firstOrNull()?.name ?: "Sem Categoria"
                                Text("Categoria: $catName", fontSize = 12.sp)
                            }

                            IconButton(onClick = { /* Ação futura */ }) {
                                Icon(Icons.Default.Edit, null, tint = PazBlack)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Iniciar Venda
            Button(
                onClick = {
                    if (selectedCommunity != null) {
                        onStartSale(selectedCommunity!!.id, isAutoService)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedCommunity != null
            ) {
                Text("Iniciar Venda", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}