package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FileUpload
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
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazWhite
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Classe auxiliar para o Carrinho
data class CartItem(val product: Product, var quantity: Int)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun PosScreen(
    communityId: Long,
    isAutoService: Boolean,
    onBackClick: () -> Unit,
    onSaleFinished: () -> Unit // Quando fecha o caixa/venda
) {
    val coroutineScope = rememberCoroutineScope()

    var currentTemp by remember { mutableStateOf("00") }

    // Dados
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var cart by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Estado da Venda
    var currentSaleId by remember { mutableStateOf<Long?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("DINHEIRO") }

    // --- ESTADOS PARA O FECHAMENTO ---
    var showCloseDialog by remember { mutableStateOf(false) }
    var closingObservation by remember { mutableStateOf("") }
    var proofImagesBase64 by remember { mutableStateOf<List<String>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }

    // Filtro de busca
    var searchText by remember { mutableStateOf("") }

    // Totais
    val totalAmount = cart.sumOf { it.product.salePrice * it.quantity }

    // --- CORREÇÃO AQUI ---
    // Configuração do FileKit para pegar imagens (Galeria/Câmera)
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Multiple(), // <--- ADICIONADO () AQUI
        title = "Selecione Comprovantes"
    ) { files ->
        files?.let { fileList ->
            // Processamento em background
            val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default)
            scope.launch {
                val newImages = fileList.map { file ->
                    val bytes = file.readBytes()
                    Base64.encode(bytes)
                }
                // Adiciona às já existentes na lista (Voltando para a Main Thread se necessário, mas o State segura)
                proofImagesBase64 = proofImagesBase64 + newImages
            }
        }
    }

    // Inicialização
    LaunchedEffect(Unit) {
        isLoading = true
        products = ApiClient.getAllProducts()

        // Cria a venda (Sessão) ao entrar
        val saleReq = SaleRequest(communityId, isAutoService)
        val sale = ApiClient.createSale(saleReq)
        if (sale != null) {
            currentSaleId = sale.id
        } else {
            onBackClick() // Erro crítico se não conseguir criar venda
        }
        isLoading = false
    }

    // Função para encerrar a venda (Fechar Caixa)
    fun closeSale() {
        if (currentSaleId != null) {
            isUploading = true
            coroutineScope.launch {
                // 1. Se tiver imagens, faz upload primeiro
                if (proofImagesBase64.isNotEmpty()) {
                    ApiClient.uploadSaleImages(currentSaleId!!, proofImagesBase64)
                }

                // 2. Finaliza a venda no backend com a observação
                ApiClient.completeSale(currentSaleId!!, closingObservation)

                isUploading = false
                showCloseDialog = false
                onSaleFinished() // Sai da tela
            }
        }
    }

    val filteredProducts = products.filter { it.name.contains(searchText, ignoreCase = true) }

    // --- O NOVO DIÁLOGO DE FECHAMENTO ---
    if (showCloseDialog) {
        Dialog(onDismissRequest = { if (!isUploading) showCloseDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PazWhite),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Fechar Caixa", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Deseja encerrar as vendas deste ponto? Adicione comprovantes se necessário.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    // Campo de Observação (Opcional)
                    OutlinedTextField(
                        value = closingObservation,
                        onValueChange = { closingObservation = it },
                        label = { Text("Observação do dia (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    // Botão para Adicionar Fotos
                    OutlinedButton(
                        onClick = { launcher.launch() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.FileUpload, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Adicionar Comprovantes/Fotos")
                    }

                    // Lista Horizontal de Miniaturas selecionadas
                    if (proofImagesBase64.isNotEmpty()) {
                        Text(
                            "${proofImagesBase64.size} imagens selecionadas",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(proofImagesBase64) { base64 ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                ) {
                                    // Mostra a imagem decodificada do Base64
                                    AsyncImage(
                                        model = "data:image/jpeg;base64,$base64",
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Botãozinho X para remover
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = "Remover",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(20.dp)
                                            .background(PazWhite, CircleShape)
                                            .clickable {
                                                proofImagesBase64 = proofImagesBase64 - base64
                                            }
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Botões de Ação (Voltar vs Encerrar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // BOTÃO VOLTAR (Cancela a ação de fechar, continua vendendo)
                        OutlinedButton(
                            onClick = { showCloseDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isUploading
                        ) {
                            Text("Voltar", color = PazBlack)
                        }

                        // BOTÃO ENCERRAR (Finaliza tudo)
                        Button(
                            onClick = { closeSale() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isUploading
                        ) {
                            if (isUploading) {
                                CircularProgressIndicator(color = PazWhite, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Encerrar")
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(PazBlack)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Ícone de Configuração (No Auto, é por aqui que sai)
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Config",
                        tint = PazWhite,
                        modifier = Modifier.align(Alignment.CenterStart).clickable {
                            // Se for Auto, pede confirmação para sair. Se for Manual, só volta.
                            if (isAutoService) showCloseDialog = true else onBackClick()
                        }
                    )
                    Text(
                        if (isAutoService) "Auto Atendimento" else "Venda Manual #${currentSaleId ?: ""}",
                        color = PazWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Text(
                        "${currentTemp}°🌡️",
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

            // Busca
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar produto...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PazBlack
                    ),
                    trailingIcon = { Icon(Icons.Default.Search, null) }
                )
            }

            // --- GRADE DE PRODUTOS ---
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

            // --- CARRINHO E FINALIZAÇÃO ---
            if (cart.isNotEmpty() || !isAutoService) { // Manual sempre mostra a barra inferior
                Surface(
                    color = Color(0xFF2D2D35),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Só mostra lista do carrinho se tiver itens
                        if (cart.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ShoppingCart, null, tint = PazWhite)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Carrinho", color = PazWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }

                            LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                                items(cart) { item ->
                                    Row(
                                        Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(item.product.name, Modifier.weight(1.5f), color = PazWhite, fontSize = 14.sp)

                                        // Controle Qtd
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

                                        // Preço Total do Item
                                        Text("R$ ${item.product.salePrice * item.quantity}", Modifier.weight(1f), color = PazWhite, textAlign = TextAlign.End)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Pagamento e Total
                            Text("Total: R$ $totalAmount", color = PazWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                PaymentOption("Cartão", selectedPaymentMethod == "CARTAO") { selectedPaymentMethod = "CARTAO" }
                                PaymentOption("Pix", selectedPaymentMethod == "PIX") { selectedPaymentMethod = "PIX" }
                                PaymentOption("Dinheiro", selectedPaymentMethod == "DINHEIRO") { selectedPaymentMethod = "DINHEIRO" }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // BOTÃO: FINALIZAR PEDIDO (Cria um pedido dentro da venda)
                            Button(
                                onClick = {
                                    if (currentSaleId != null) {
                                        coroutineScope.launch {
                                            val itemsReq = cart.map { ItemOrderRequest(it.product.id, it.quantity, it.product.salePrice) }
                                            val orderReq = OrderRequest(selectedPaymentMethod, itemsReq)
                                            if (ApiClient.createOrder(currentSaleId!!, orderReq)) {
                                                cart = emptyList() // Limpa carrinho
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF464F41)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("CONFIRMAR PEDIDO", color = PazWhite, fontWeight = FontWeight.Bold)
                            }
                        }

                        // BOTÃO: ENCERRAR CAIXA (Só aparece no Manual)
                        if (!isAutoService) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = { showCloseDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, Color.Red),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Text("ENCERRAR CAIXA (Fim do Dia)")
                            }
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
            // Imagem com Coil
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (product.images.isNotEmpty()) {
                    AsyncImage(
                        model = product.images[0].url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Coffee, null, tint = Color(0xFF5D4037), modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)

            // CORRIGIDO: salePrice
            Text("R$ ${product.salePrice}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)

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