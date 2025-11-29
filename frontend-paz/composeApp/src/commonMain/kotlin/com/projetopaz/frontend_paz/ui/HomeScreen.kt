package com.projetopaz.frontend_paz.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.SaleResponse
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToCommunities: () -> Unit,
    onNavigateToSales: () -> Unit,
    onNavigateToDetails: (SaleResponse) -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSuppliers: () -> Unit, // <--- NOVO
    onNavigateToProfile: () -> Unit,   // <--- NOVO
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var sales by remember { mutableStateOf<List<SaleResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isFabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        sales = ApiClient.getAllSales()
        isLoading = false
    }

    Scaffold(
        containerColor = PazGrayBgEnd,
        floatingActionButton = {
            // Menu Flutuante
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AnimatedVisibility(visible = isFabExpanded, enter = fadeIn() + slideInVertically { 40 }, exit = fadeOut() + slideOutVertically { 40 }) {
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        FabOption("Vender", Icons.Outlined.ShoppingCart, onNavigateToSales)
                        FabOption("Estoque", Icons.Outlined.Inventory, onNavigateToProducts)
                        FabOption("Comunidades", Icons.Outlined.People, onNavigateToCommunities)
                        FabOption("Fornecedores", Icons.Outlined.LocalShipping, onNavigateToSuppliers) // <--- BOTÃO NOVO
                        FabOption("Categorias", Icons.Default.Category, onNavigateToCategories)
                        FabOption("Fechar", Icons.Default.Close) { isFabExpanded = false }
                    }
                }
                if (!isFabExpanded) {
                    FloatingActionButton(onClick = { isFabExpanded = true }, containerColor = PazBlack, contentColor = PazWhite, shape = CircleShape, modifier = Modifier.size(64.dp)) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                // Header
                Box(modifier = Modifier.fillMaxWidth().background(PazWhite).padding(24.dp)) {
                    Column {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            // Ícone de Perfil Clicável
                            IconButton(onClick = onNavigateToProfile) { // <--- CLICK NO PERFIL
                                Icon(Icons.Default.Person, null, modifier = Modifier.size(48.dp), tint = PazBlack)
                            }
                            Text("25°🌡️", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PazBlack)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Seja bem-vindo!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = PazBlack)
                        Text("Nosso aplicativo visa facilitar e organizar suas vendas.", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text("Veja as ultimas vendas:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PazBlack)
                    Spacer(Modifier.height(8.dp))
                    if (isLoading) {
                        Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PazBlack) }
                    } else if (sales.isEmpty()) {
                        Text("Nenhuma venda registrada.", color = Color.Gray, modifier = Modifier.padding(top=16.dp))
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                            items(sales) { sale ->
                                SaleHistoryCard(sale, onDetailClick = { onNavigateToDetails(sale) })
                            }
                        }
                    }
                }
            }
            if (isFabExpanded) {
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).clickable { isFabExpanded = false })
            }
        }
    }
}

// ... (Mantenha o SaleHistoryCard e FabOption como estavam antes) ...
@Composable
fun SaleHistoryCard(sale: SaleResponse, onDetailClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PazWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, PazBlack, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Venda #${sale.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text(sale.createdAt ?: "--/--", color = PazBlack, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, PazBlack, RoundedCornerShape(12.dp)).background(PazWhite),
                contentAlignment = Alignment.Center
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, null); Text("Foto comprovante", fontWeight = FontWeight.Bold); Icon(Icons.Default.ArrowForward, null)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val statusText = when(sale.status) { 1 -> "Ativa"; 2 -> "Concluída"; else -> "Cancelada" }
                Text("Status: $statusText", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text("Metodo: ${if(sale.isSelfService) "Auto" else "Manual"}", fontWeight = FontWeight.Bold, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onDetailClick,
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(45.dp)
            ) { Text("Ver Mais", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}

@Composable
fun FabOption(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(8.dp), color = PazWhite, shadowElevation = 2.dp, modifier = Modifier.padding(end = 8.dp)) {
            Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
        }
        FloatingActionButton(onClick = onClick, containerColor = PazBlack, contentColor = PazWhite, shape = CircleShape, modifier = Modifier.size(50.dp)) {
            Icon(icon, null)
        }
    }
}