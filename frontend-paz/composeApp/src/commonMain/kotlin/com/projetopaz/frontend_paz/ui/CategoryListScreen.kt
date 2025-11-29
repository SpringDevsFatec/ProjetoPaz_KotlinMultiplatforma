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
import androidx.compose.material.icons.filled.Category
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
import com.projetopaz.frontend_paz.model.Category
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    key: Int,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Category) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key) {
        isLoading = true
        categories = ApiClient.getAllCategories()
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Categorias", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = PazBlack, contentColor = PazWhite) {
                Icon(Icons.Default.Add, "Nova")
            }
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PazBlack) }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(categories) { cat ->
                    Card(
                        modifier = Modifier.clickable { onEditClick(cat) },
                        colors = CardDefaults.cardColors(containerColor = PazWhite),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Category, null, tint = PazBlack)
                            Spacer(Modifier.height(8.dp))
                            Text(cat.name ?: "Sem nome", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(cat.description ?: "", color = Color.Gray, fontSize = 12.sp, minLines = 2)

                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { onEditClick(cat) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Outlined.Edit, null, tint = PazBlack)
                                }
                                IconButton(onClick = {
                                    coroutineScope.launch { if (ApiClient.deleteCategory(cat.id)) categories = ApiClient.getAllCategories() }
                                }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Outlined.Delete, null, tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}