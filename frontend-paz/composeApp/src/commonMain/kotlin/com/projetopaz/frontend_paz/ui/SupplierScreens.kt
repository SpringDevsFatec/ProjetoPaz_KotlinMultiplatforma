package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.projetopaz.frontend_paz.model.Supplier
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

// --- LISTA DE FORNECEDORES ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(
    key: Int = 0, // Adicionado key para forçar recarga
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Supplier) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var suppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key) {
        isLoading = true
        suppliers = ApiClient.getAllSuppliers()
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Fornecedores", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = PazBlack, contentColor = PazWhite) {
                Icon(Icons.Default.Add, "Novo")
            }
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PazBlack) }
        } else if (suppliers.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Nenhum fornecedor cadastrado.", color = Color.Gray) }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(suppliers) { sup ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PazWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.clickable { onEditClick(sup) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalShipping, null, tint = PazBlack)
                                Spacer(Modifier.width(8.dp))
                                Text(sup.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }

                            Spacer(Modifier.height(8.dp))
                            if (!sup.contactName.isNullOrBlank()) Text("Contato: ${sup.contactName}", fontSize = 14.sp)
                            if (!sup.phone.isNullOrBlank()) Text("Tel: ${sup.phone}", fontSize = 14.sp, color = Color.Gray)
                            if (!sup.email.isNullOrBlank()) Text("Email: ${sup.email}", fontSize = 14.sp, color = Color.Gray)

                            Spacer(Modifier.height(12.dp))
                            Divider(color = Color(0xFFEEEEEE))

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { onEditClick(sup) }) {
                                    Icon(Icons.Outlined.Edit, "Editar", tint = PazBlack)
                                }
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        if (ApiClient.deleteSupplier(sup.id)) {
                                            suppliers = ApiClient.getAllSuppliers()
                                        }
                                    }
                                }) {
                                    Icon(Icons.Outlined.Delete, "Excluir", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- FORMULÁRIO DE FORNECEDOR ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierFormScreen(
    supplierToEdit: Supplier? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Campos
    var name by remember { mutableStateOf(supplierToEdit?.name ?: "") }
    var contactName by remember { mutableStateOf(supplierToEdit?.contactName ?: "") }
    var phone by remember { mutableStateOf(supplierToEdit?.phone ?: "") }
    var email by remember { mutableStateOf(supplierToEdit?.email ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (supplierToEdit != null) "Editar Fornecedor" else "Novo Fornecedor", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PazWhite)
            )
        },
        containerColor = PazGrayBgEnd
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PazInput("Nome da Empresa/Fornecedor *", name, { name = it }, icon = Icons.Default.Business)
            PazInput("Nome do Contato", contactName, { contactName = it }, icon = Icons.Default.Person)
            PazInput("Telefone", phone, { phone = it }, icon = Icons.Default.Phone, isNumber = true)
            PazInput("Email", email, { email = it }, icon = Icons.Default.Email)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val supplier = Supplier(
                                id = supplierToEdit?.id ?: 0,
                                name = name,
                                contactName = contactName,
                                phone = phone,
                                email = email
                            )

                            val success = if (supplierToEdit == null) {
                                ApiClient.createSupplier(supplier)
                            } else {
                                ApiClient.updateSupplier(supplier.id, supplier)
                            }

                            if (success) onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(8.dp)
            ) { Text("SALVAR", fontWeight = FontWeight.Bold) }
        }
    }
}