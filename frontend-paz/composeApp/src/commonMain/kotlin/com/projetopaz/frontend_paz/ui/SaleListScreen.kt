package com.projetopaz.frontend_paz.ui

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun SaleListScreen(
    onBackClick: () -> Unit,
    onSaleClick: (SaleResponse) -> Unit
) {
    var allSales by remember { mutableStateOf<List<SaleResponse>>(emptyList()) }
    var filteredSales by remember { mutableStateOf<List<SaleResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // --- ESTADOS DO FILTRO ---
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterId by remember { mutableStateOf("") }
    var filterDate by remember { mutableStateOf("") } // DD/MM/AAAA
    var filterStatus by remember { mutableStateOf<Int?>(null) } // null=Todos, 1=Ativa, 0=Cancelada, 2=Concluída

    // Carrega vendas ao abrir
    LaunchedEffect(Unit) {
        isLoading = true
        val sales = ApiClient.getAllSales()
        allSales = sales.sortedByDescending { it.id } // Mais recentes primeiro
        filteredSales = allSales
        isLoading = false
    }

    // Lógica de Filtragem Local
    fun applyFilters() {
        filteredSales = allSales.filter { sale ->
            // Filtro por ID
            val matchId = if (filterId.isBlank()) true else sale.id.toString().contains(filterId)

            // Filtro por Data (Busca simples na string)
            val dateBackend = sale.createdAt ?: ""
            // Converte "2025-12-25" para "25122025" para bater com o input
            val dateUi = formatBackendDateToUi(dateBackend)
            val filterDateClean = filterDate.filter { it.isDigit() }
            val matchDate = if (filterDateClean.isBlank()) true else dateUi.contains(filterDateClean)

            // Filtro por Status
            val matchStatus = if (filterStatus == null) true else sale.status == filterStatus

            matchId && matchDate && matchStatus
        }
        showFilterSheet = false
    }

    fun clearFilters() {
        filterId = ""
        filterDate = ""
        filterStatus = null
        filteredSales = allSales
        showFilterSheet = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Histórico de Vendas", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                actions = {
                    // ÍCONE DE FILTRO (Abre o Pop-up)
                    IconButton(onClick = { showFilterSheet = true }) {
                        val isFiltering = filterId.isNotEmpty() || filterDate.isNotEmpty() || filterStatus != null
                        Icon(Icons.Default.FilterList, "Filtrar", tint = if (isFiltering) Color(0xFF4CAF50) else PazBlack)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PazBlack)
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text(
                    "${filteredSales.size} vendas encontradas",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (filteredSales.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhuma venda encontrada.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredSales) { sale ->
                            SaleHistoryCard(sale, onClick = { onSaleClick(sale) })
                        }
                    }
                }
            }
        }
    }

    // --- POP-UP (MODAL BOTTOM SHEET) ---
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = PazWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Filtrar Vendas", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Divider(color = Color.LightGray)

                // 1. ID
                PazInput("ID da Venda", filterId, { if (it.all { c -> c.isDigit() }) filterId = it })

                // 2. Data
                PazInput(
                    label = "Data (DD/MM/AAAA)",
                    value = filterDate,
                    onValueChange = { if (it.all { c -> c.isDigit() }) filterDate = it },
                    visualTransformation = DateTransformation(),
                    maxLength = 8
                )

                // 3. Status (Chips)
                Text("Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = filterStatus == null,
                        onClick = { filterStatus = null },
                        label = { Text("Todos") }
                    )
                    FilterChip(
                        selected = filterStatus == 1,
                        onClick = { filterStatus = 1 },
                        label = { Text("Ativas") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFE0E0E0))
                    )
                    FilterChip(
                        selected = filterStatus == 2,
                        onClick = { filterStatus = 2 },
                        label = { Text("Concluídas", color = if (filterStatus == 2) PazWhite else PazBlack) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PazBlack)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Botões de Ação
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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