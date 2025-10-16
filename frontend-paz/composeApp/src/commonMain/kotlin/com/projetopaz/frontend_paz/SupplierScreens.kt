package com.projetopaz.frontend_paz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.network.Supplier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(
    onAddClick: () -> Unit,
    onEditClick: (Supplier) -> Unit,
    onBackClick: () -> Unit
) {
    var suppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    fun refreshSuppliers() {
        isLoading = true
        coroutineScope.launch {
            suppliers = ApiClient.getAllSuppliers()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        refreshSuppliers()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Fornecedores") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Adicionar Fornecedor", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(suppliers) { supplier ->
                        SupplierCardItem(
                            supplier = supplier,
                            onEditClick = { onEditClick(supplier) },
                            onDeleteClick = {
                                coroutineScope.launch {
                                    if (ApiClient.deleteSupplier(supplier.id!!)) {
                                        refreshSuppliers()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SupplierCardItem(
    supplier: Supplier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = supplier.name ?: "Fornecedor sem nome",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Contato: ${supplier.contactName ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, "Excluir", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierFormScreen(
    supplier: Supplier?,
    onSave: () -> Unit,
    onBackClick: () -> Unit
) {
    val isEditing = supplier != null
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf(supplier?.name ?: "") }
    var contactName by remember { mutableStateOf(supplier?.contactName ?: "") }
    var phone by remember { mutableStateOf(supplier?.phone ?: "") }
    var email by remember { mutableStateOf(supplier?.email ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Fornecedor" else "Novo Fornecedor") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = contactName, onValueChange = { contactName = it }, label = { Text("Nome do Contato") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val supplierData = Supplier(
                            id = supplier?.id,
                            name = name,
                            contactName = contactName,
                            phone = phone,
                            email = email
                        )
                        val success = if (isEditing) {
                            ApiClient.updateSupplier(supplier!!.id!!, supplierData)
                        } else {
                            ApiClient.createSupplier(supplierData)
                        }
                        if (success) {
                            onSave()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (isEditing) "Salvar Alterações" else "Cadastrar Fornecedor")
            }
        }
    }
}