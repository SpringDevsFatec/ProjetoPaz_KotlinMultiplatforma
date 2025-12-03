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
    onNavigateToSuppliers: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSalesHistory: () -> Unit, // <--- NOVO PARÂMETRO
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

                        // Opções do Menu
                        FabOption("Vender", Icons.Outlined.ShoppingCart, onNavigateToSales)
                        FabOption("Histórico", Icons.Default.History, onNavigateToSalesHistory) // <--- AQUI O HISTÓRICO
                        FabOption("Estoque", Icons.Outlined.Inventory, onNavigateToProducts)
                        FabOption("Comunidades", Icons.Outlined.People, onNavigateToCommunities)
                        FabOption("Fornecedores", Icons.Outlined.LocalShipping, onNavigateToSuppliers)
                        FabOption("Categorias", Icons.Default.Category, onNavigateToCategories)

                        FabOption("Fechar", Icons.Default.Close) { isFabExpanded = false }
                    }
                }

                // Botão Principal (+)
                if (!isFabExpanded) {
                    FloatingActionButton(
                        onClick = { isFabExpanded = true },
                        containerColor = PazBlack,
                        contentColor = PazWhite,
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp)
                    ) {
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
                            IconButton(onClick = onNavigateToProfile) {
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

                // Lista de Vendas Recentes
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text("Veja as ultimas vendas:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PazBlack)
                    Spacer(Modifier.height(8.dp))

                    if (isLoading) {
                        Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PazBlack) }
                    } else if (sales.isEmpty()) {
                        Text("Nenhuma venda registrada.", color = Color.Gray, modifier = Modifier.padding(top=16.dp))
                    } else {
                        // Mostra apenas as 5 últimas na Home para não ficar pesado
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(sales.take(5)) { sale ->
                                SaleHistoryCard(sale, onClick = { onNavigateToDetails(sale) })
                            }
                        }
                    }
                }
            }

            // Overlay Escuro quando o menu abre
            if (isFabExpanded) {
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).clickable { isFabExpanded = false })
            }
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