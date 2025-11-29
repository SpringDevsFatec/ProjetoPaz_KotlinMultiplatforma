package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // --- ESTADOS ---
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var ddd1 by remember { mutableStateOf("") }
    var phone1 by remember { mutableStateOf("") }
    var ddd2 by remember { mutableStateOf("") }
    var phone2 by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var quarter by remember { mutableStateOf("") }
    var complement by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        // --- AQUI ESTÁ A CORREÇÃO: BARRA SUPERIOR COM BOTÃO VOLTAR ---
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cadastrar Usuário", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = PazBlack)
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
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Text("PAZ", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // --- USUÁRIO ---
            SectionTitle("Usuário")
            PazInput("Nome *", name, { name = it }, icon = Icons.Default.Person)
            PazInput("Sobrenome", surname, { surname = it }, icon = Icons.Default.PersonOutline)
            PazInput("Email *", email, { email = it }, icon = Icons.Default.Email)
            PazInput("Senha (min 6) *", password, { password = it }, icon = Icons.Default.Lock, isPassword = true)

            PazInput(
                label = "Data de Nascimento (DD/MM/AAAA) *",
                value = birthday,
                onValueChange = { if (it.all { c -> c.isDigit() }) birthday = it },
                icon = Icons.Default.CalendarToday,
                placeholder = "23032005",
                isNumber = true,
                maxLength = 8,
                visualTransformation = DateTransformation()
            )

            Divider(Modifier.padding(vertical = 8.dp))

            // --- TELEFONE ---
            SectionTitle("Telefone")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(0.3f)) { PazInput("DDD *", ddd1, { ddd1 = it }, isNumber = true, maxLength = 2) }
                Box(Modifier.weight(0.7f)) { PazInput("Celular 1 *", phone1, { phone1 = it }, icon = Icons.Default.Phone, isNumber = true, maxLength = 9) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(0.3f)) { PazInput("DDD 2", ddd2, { ddd2 = it }, isNumber = true, maxLength = 2) }
                Box(Modifier.weight(0.7f)) { PazInput("Celular 2", phone2, { phone2 = it }, icon = Icons.Default.Phone, isNumber = true, maxLength = 9) }
            }

            Divider(Modifier.padding(vertical = 8.dp))

            // --- ENDEREÇO ---
            SectionTitle("Endereço")
            PazInput("CEP *", cep, { cep = it }, icon = Icons.Default.Place, isNumber = true, maxLength = 8, visualTransformation = CepTransformation())
            PazInput("Rua *", street, { street = it }, icon = Icons.Default.Home)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(0.4f)) { PazInput("Número *", number, { number = it }, isNumber = true) }
                Box(Modifier.weight(0.6f)) { PazInput("Bairro *", quarter, { quarter = it }) }
            }
            PazInput("Complemento", complement, { complement = it })

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    // Validação simples
                    if (name.isNotBlank() && email.isNotBlank() && password.length >= 6 &&
                        ddd1.isNotBlank() && phone1.isNotBlank() &&
                        cep.isNotBlank() && street.isNotBlank() && number.isNotBlank() && quarter.isNotBlank() &&
                        birthday.length == 8) {

                        isLoading = true
                        errorMessage = null

                        coroutineScope.launch {
                            val phones = mutableListOf(Cellphone("+55", ddd1, null, phone1, null))
                            if (ddd2.isNotBlank() && phone2.isNotBlank()) {
                                phones.add(Cellphone("+55", ddd2, null, phone2, null))
                            }

                            val addressObj = Address(street, cep, quarter, number, complement)
                            val backendDate = convertDateToBackendFormat(birthday)

                            val request = UserCreateRequest(
                                name, surname, backendDate, email, password, null, addressObj, phones
                            )

                            if (ApiClient.register(request)) {
                                onRegisterSuccess()
                            } else {
                                errorMessage = "Erro ao cadastrar. Tente outro email."
                                isLoading = false
                            }
                        }
                    } else {
                        errorMessage = "Preencha os campos obrigatórios corretamente."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(25.dp),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = PazWhite)
                else Text("CADASTRAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}