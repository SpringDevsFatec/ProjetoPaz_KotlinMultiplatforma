package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.CellphoneSupplier
import com.projetopaz.frontend_paz.model.Supplier
import com.projetopaz.frontend_paz.model.SupplierAddress
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

// --- TELA DE LISTAGEM ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(
    key: Int,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Supplier) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var suppliers by remember { mutableStateOf<List<Supplier>>(emptyList()) }

    LaunchedEffect(key) {
        suppliers = ApiClient.getAllSuppliers()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Fornecedores", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suppliers) { supplier ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = PazWhite),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(supplier.name ?: "Sem Nome", fontWeight = FontWeight.Bold)
                            Text(supplier.occupation ?: "Sem Razão Social", fontSize = 12.sp, color = Color.Gray)
                            if (supplier.cellphones.isNotEmpty()) {
                                Text("Tel: ${supplier.cellphones[0].phone1}", fontSize = 12.sp)
                            }
                        }
                        Row {
                            IconButton(onClick = { onEditClick(supplier) }) { Icon(Icons.Default.Edit, null, tint = PazBlack) }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    if (ApiClient.deleteSupplier(supplier.id)) {
                                        suppliers = ApiClient.getAllSuppliers()
                                    }
                                }
                            }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}

// --- TELA DE FORMULÁRIO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierFormScreen(
    supplierToEdit: Supplier? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Dados Gerais
    var name by remember { mutableStateOf(supplierToEdit?.name ?: "") }
    var razaoSocial by remember { mutableStateOf(supplierToEdit?.occupation ?: "") }
    var cnpj by remember { mutableStateOf(supplierToEdit?.cnpj ?: "") }
    var type by remember { mutableStateOf(supplierToEdit?.type ?: "") }
    var observation by remember { mutableStateOf(supplierToEdit?.observation ?: "") }

    // Telefone
    val firstPhone = supplierToEdit?.cellphones?.firstOrNull()
    var countryNumber by remember { mutableStateOf(firstPhone?.countryNumber ?: "+55") }
    var ddd1 by remember { mutableStateOf(firstPhone?.ddd1 ?: "") }
    var ddd2 by remember { mutableStateOf(firstPhone?.ddd2 ?: "") }
    var phone1 by remember { mutableStateOf(firstPhone?.phone1 ?: "") }
    var phone2 by remember { mutableStateOf(firstPhone?.phone2 ?: "") }

    // Endereço
    val firstAddr = supplierToEdit?.addresses?.firstOrNull()
    var cep by remember { mutableStateOf(firstAddr?.cep ?: "") }
    var rua by remember { mutableStateOf(firstAddr?.street ?: "") }
    var numero by remember { mutableStateOf(firstAddr?.number ?: "") }
    var bairro by remember { mutableStateOf(firstAddr?.neighborhood ?: "") }
    var complemento by remember { mutableStateOf(firstAddr?.complement ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (supplierToEdit != null) "Editar" else "Novo Fornecedor", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. DADOS GERAIS
            PazInput("Nome", name, { name = it })
            PazInput("Razão Social", razaoSocial, { razaoSocial = it })
            // Aplica Máscara de CNPJ
            PazInput("CNPJ/CPF", cnpj, { cnpj = it }, visualTransformation = CnpjTransformation())
            PazInput("Tipo de Fornecedor", type, { type = it })
            PazInput("Observação", observation, { observation = it })

            Spacer(Modifier.height(10.dp))

// SEÇÃO 2: TELEFONE
            Text("Telefone", fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = PazWhite)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PazInput("País (+55)", countryNumber, { countryNumber = it })

                    PazInput("DDD 1", ddd1, { ddd1 = it }) // DDD sem máscara (são só 2 números)
                    // AQUI MUDOU: Usa a máscara sem parênteses
                    PazInput("Número 1", phone1, { phone1 = it }, visualTransformation = CellNumberTransformation())

                    PazInput("DDD 2", ddd2, { ddd2 = it })
                    // AQUI MUDOU: Usa a máscara sem parênteses
                    PazInput("Número 2", phone2, { phone2 = it }, visualTransformation = CellNumberTransformation())
                }
            }

            Spacer(Modifier.height(10.dp))

            // 3. ENDEREÇO
            Text("Endereço", fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = PazWhite)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Aplica Máscara de CEP
                    PazInput("CEP", cep, { cep = it }, visualTransformation = CepTransformation())
                    PazInput("Rua", rua, { rua = it })
                    PazInput("Número", numero, { numero = it })
                    PazInput("Bairro", bairro, { bairro = it })
                    PazInput("Complemento", complemento, { complemento = it })
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            // Empacota os dados nas listas
                            val newPhones = listOf(CellphoneSupplier(
                                countryNumber = countryNumber, ddd1 = ddd1, ddd2 = ddd2, phone1 = phone1, phone2 = phone2
                            ))
                            val newAddresses = listOf(SupplierAddress(
                                cep = cep, street = rua, number = numero, neighborhood = bairro, complement = complemento, city = "", state = ""
                            ))

                            val supplier = Supplier(
                                id = supplierToEdit?.id ?: 0,
                                name = name,
                                occupation = razaoSocial,
                                cnpj = cnpj,
                                type = type,
                                observation = observation,
                                cellphones = newPhones,
                                addresses = newAddresses
                            )

                            val success = if (supplierToEdit == null) ApiClient.createSupplier(supplier) else ApiClient.updateSupplier(supplier.id, supplier)
                            if (success) onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SALVAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Helper Customizado para os Inputs com Máscara
@Composable
fun PazInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = {
                // Se tiver máscara, só deixa digitar números para não quebrar a formatação
                if (visualTransformation != VisualTransformation.None) {
                    if (it.all { char -> char.isDigit() }) onValueChange(it)
                } else {
                    onValueChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = visualTransformation, // <--- A MÁGICA ACONTECE AQUI
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PazWhite, unfocusedContainerColor = PazWhite)
        )
    }
}