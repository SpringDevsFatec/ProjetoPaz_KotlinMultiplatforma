package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ZoomIn
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
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.projetopaz.frontend_paz.model.SaleResponse
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreen(
    sale: SaleResponse,
    onBackClick: () -> Unit
) {
    // Estado para controlar qual imagem está em tela cheia (Zoom)
    var zoomedImageUrl by remember { mutableStateOf<String?>(null) }

    // DIÁLOGO DE ZOOM (Tela Cheia)
    if (zoomedImageUrl != null) {
        Dialog(onDismissRequest = { zoomedImageUrl = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { zoomedImageUrl = null }, // Clica fora/dentro para fechar
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = zoomedImageUrl,
                    contentDescription = "Imagem Ampliada",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                // Botão fechar no topo
                IconButton(
                    onClick = { zoomedImageUrl = null },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(Icons.Default.Close, "Fechar", tint = PazWhite)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Histórico de venda", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("#${sale.id}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Card Principal
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PazWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxSize().border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // --- SEÇÃO COMPROVANTE (CARROSSEL) ---
                    Text("Comprovantes", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (sale.images.isNotEmpty()) {
                        // Carrossel de Imagens
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().height(150.dp)
                        ) {
                            items(sale.images) { img ->
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                        .clickable { zoomedImageUrl = img.url } // Clica para ampliar
                                ) {
                                    AsyncImage(
                                        model = img.url,
                                        contentDescription = img.alt,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Ícone de lupa no canto para indicar zoom
                                    Icon(
                                        Icons.Default.ZoomIn,
                                        contentDescription = null,
                                        tint = PazWhite,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(8.dp)
                                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    )
                                }
                            }
                        }
                    } else {
                        // Estado Vazio (Placeholder)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, PazBlack, RoundedCornerShape(12.dp))
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Image, null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sem foto comprovante", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Infos Gerais
                    InfoRow("Método de venda:", if (sale.isSelfService) "Auto Atendimento" else "Manual")
                    InfoRow("Status de venda:", when(sale.status) { 1 -> "Ativa"; 2 -> "Concluída"; else -> "Cancelada" })

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Observação da venda:", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(sale.observation ?: "Nenhuma observação.", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resumo Financeiro
                    val total = sale.orders.sumOf { it.total }
                    val itemsCount = sale.orders.sumOf { it.items.sumOf { item -> item.quantity } }

                    Text("Número total de pedidos: $itemsCount", fontWeight = FontWeight.Bold)
                    Text("Forma de pagamento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider(color = PazBlack, thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val methods = sale.orders.joinToString(", ") { it.paymentMethod }
                        Text(methods.ifEmpty { "Nenhum" }, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabela de Itens (Nota)
                    Text("Nota:", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Ocupa o resto do espaço
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                            .border(1.dp, PazBlack, RoundedCornerShape(12.dp))
                    ) {
                        LazyColumn(contentPadding = PaddingValues(12.dp)) {
                            item {
                                Row(Modifier.fillMaxWidth()) {
                                    Text("PRODUTO", Modifier.weight(2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("QTD", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("VALOR", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                HorizontalDivider(color = Color.Gray)
                            }

                            // Itera sobre todos os itens de todos os pedidos da venda
                            val allItems = sale.orders.flatMap { it.items }
                            items(allItems) { item ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Text("Prod #${item.productId}", Modifier.weight(2f), fontSize = 12.sp)
                                    Text("${item.quantity}", Modifier.weight(1f), fontSize = 12.sp)
                                    Text("R$ ${item.unitPrice}", Modifier.weight(1f), fontSize = 12.sp)
                                }
                                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    Text("TOTAL: R$ $total", fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("VOLTAR PARA O HUB", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}