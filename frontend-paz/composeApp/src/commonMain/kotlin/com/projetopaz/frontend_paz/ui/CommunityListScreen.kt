package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.Community
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityListScreen(
    key: Int,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Community) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var communities by remember { mutableStateOf<List<Community>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key) {
        isLoading = true
        communities = ApiClient.getAllCommunities()
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Comunidades", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PazBlack) }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(communities) { comm ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onEditClick(comm) },
                        colors = CardDefaults.cardColors(containerColor = PazWhite),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(20.dp))
                                Text(comm.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start=8.dp))
                            }
                            Text(comm.description, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top=4.dp))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text("📍 ${comm.quarter}, ${comm.number}", fontSize = 14.sp)
                            Text("CEP: ${comm.cep}", fontSize = 13.sp, color = Color.Gray)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { onEditClick(comm) }) { Icon(Icons.Outlined.Edit, "Editar", tint = PazBlack) }
                                IconButton(onClick = {
                                    coroutineScope.launch { if (ApiClient.deleteCommunity(comm.id)) communities = ApiClient.getAllCommunities() }
                                }) { Icon(Icons.Outlined.Delete, "Excluir", tint = Color.Red) }
                            }
                        }
                    }
                }
            }
        }
    }
}