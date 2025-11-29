package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

// Classe auxiliar para o Carrinho
data class CartItem(val product: Product, var quantity: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    communityId: Long,
    isAutoService: Boolean,
    onBackClick: () -> Unit,
    onSaleFinished: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Dados
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    // Carrinho agora é uma lista observável para atualizar a UI corretamente
    var cart by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Estado da Venda
    var currentSaleId by remember { mutableStateOf<Long?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("DINHEIRO") } // Padrão

    // Filtro de busca
    var searchText by remember { mutableStateOf("") }

    // Totais
    val totalAmount = cart.sumOf { it.product.price * it.quantity }

    // Inicialização
    LaunchedEffect(Unit) {
        isLoading = true
        products = ApiClient.getAllProducts()

        // Cria a venda ao entrar
        val saleReq = SaleRequest(communityId, isAutoService)
        val sale = ApiClient.createSale(saleReq)
        if (sale != null) {
            currentSaleId = sale.id
        } else {
            onBackClick() // Erro ao criar venda
        }
        isLoading = false
    }

    val filteredProducts = products.filter { it.name?.contains(searchText, ignoreCase = true) == true }

    Scaffold(
        topBar = {
            // Header Escuro com Temperatura igual ao Figma
            Column(modifier = Modifier.fillMaxWidth().background(PazBlack)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Config",
                        tint = PazWhite,
                        modifier = Modifier.align(Alignment.CenterStart).clickable { onBackClick() }
                    )
                    Text(
                        "25°🌡️",
                        color = PazWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        },
        containerColor = PazWhite
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Título e Busca
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    if (isAutoService) "Auto Atendimento" else "Venda Manual",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PazBlack
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar produto...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50), // Bem redondo
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PazBlack
                    ),
                    trailingIcon = { Icon(Icons.Default.FilterList, null) }
                )
            }

            // --- GRADE DE PRODUTOS (Scrollável) ---
            Box(modifier = Modifier.weight(1f)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PazBlack)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductGridItem(product) {
                                // Lógica de Adicionar: Se já existe, +1, senão cria
                                val existingIndex = cart.indexOfFirst { it.product.id == product.id }
                                if (existingIndex >= 0) {
                                    val updatedCart = cart.toMutableList()
                                    updatedCart[existingIndex] = updatedCart[existingIndex].copy(quantity = updatedCart[existingIndex].quantity + 1)
                                    cart = updatedCart
                                } else {
                                    cart = cart + CartItem(product, 1)
                                }
                            }
                        }
                    }
                }
            }

            // --- CARRINHO FIXO NO RODAPÉ (Estilo Figma Escuro) ---
            if (cart.isNotEmpty()) {
                Surface(
                    color = Color(0xFF2D2D35), // Cor escura do Figma
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShoppingCart, null, tint = PazWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Carrinho", color = PazWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cabeçalho da Tabela do Carrinho
                        Row(Modifier.fillMaxWidth()) {
                            Text("Produtos", Modifier.weight(1.5f), color = PazWhite, fontSize = 12.sp)
                            Text("quantidade", Modifier.weight(1f), color = PazWhite, fontSize = 12.sp, textAlign = TextAlign.Center)
                            Text("preço produto", Modifier.weight(1f), color = PazWhite, fontSize = 12.sp, textAlign = TextAlign.End)
                        }

                        Divider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                        // Lista de Itens no Carrinho (Limitada a altura pequena pra não ocupar a tela toda)
                        LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                            items(cart) { item ->
                                Row(
                                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.product.name ?: "", Modifier.weight(1.5f), color = PazWhite, fontSize = 14.sp)

                                    // Controle de Quantidade (- 1 +)
                                    Row(
                                        Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("-", color = PazWhite, modifier = Modifier.clickable {
                                            if (item.quantity > 1) {
                                                val idx = cart.indexOf(item)
                                                val newCart = cart.toMutableList()
                                                newCart[idx] = item.copy(quantity = item.quantity - 1)
                                                cart = newCart
                                            } else {
                                                // Remove se for 0
                                                cart = cart - item
                                            }
                                        }.padding(horizontal = 8.dp))

                                        Text("${item.quantity}", color = PazWhite)

                                        Text("+", color = PazWhite, modifier = Modifier.clickable {
                                            val idx = cart.indexOf(item)
                                            val newCart = cart.toMutableList()
                                            newCart[idx] = item.copy(quantity = item.quantity + 1)
                                            cart = newCart
                                        }.padding(horizontal = 8.dp))
                                    }

                                    Text("${item.product.price * item.quantity}$", Modifier.weight(1f), color = PazWhite, textAlign = TextAlign.End)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Forma de pagamento:", color = PazWhite, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Seleção de Pagamento (Radio Buttons)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PaymentOption("Cartão", selectedPaymentMethod == "CARTAO") { selectedPaymentMethod = "CARTAO" }
                            PaymentOption("Pix", selectedPaymentMethod == "PIX") { selectedPaymentMethod = "PIX" }
                            PaymentOption("Dinheiro", selectedPaymentMethod == "DINHEIRO") { selectedPaymentMethod = "DINHEIRO" }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Total: ${totalAmount}$",
                            color = PazWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botão Finalizar
                        Button(
                            onClick = {
                                if (currentSaleId != null) {
                                    coroutineScope.launch {
                                        val itemsReq = cart.map { ItemOrderRequest(it.product.id, it.quantity, it.product.price) }
                                        val orderReq = OrderRequest(selectedPaymentMethod, itemsReq)

                                        if (ApiClient.createOrder(currentSaleId!!, orderReq)) {
                                            onSaleFinished()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF464F41)), // Verde musgo do Figma
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Finalizar Pedido", color = PazWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onSelect() }) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = PazWhite, unselectedColor = Color.Gray)
        )
        Text(label, color = PazWhite, fontSize = 12.sp)
    }
}

@Composable
fun ProductGridItem(product: Product, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PazWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder de Imagem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Tenta mostrar a imagem se tiver URL, senão ícone
                Icon(Icons.Default.Coffee, null, tint = Color(0xFF5D4037), modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(product.name ?: "Produto", fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Text("${product.price}$", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(8.dp))

            // Botão "+" preto largo
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2D35)),
                modifier = Modifier.fillMaxWidth().height(35.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", color = PazWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}