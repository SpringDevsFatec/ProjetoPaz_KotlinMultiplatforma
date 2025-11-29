package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.projetopaz.frontend_paz.model.*
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    onBackClick: () -> Unit
) {
    val user = ApiClient.currentUser
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Preenche com os dados do usuário logado (ou vazio se der erro)
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }

    // Pega o primeiro telefone da lista, se existir
    var phone by remember { mutableStateOf(user?.cellphones?.firstOrNull()?.cellphone1 ?: "") }
    var ddd by remember { mutableStateOf(user?.cellphones?.firstOrNull()?.ddd1 ?: "") }

    // Endereço
    var cep by remember { mutableStateOf(user?.adress?.cep ?: "") }
    var street by remember { mutableStateOf(user?.adress?.street ?: "") }
    var number by remember { mutableStateOf(user?.adress?.number ?: "") }
    var quarter by remember { mutableStateOf(user?.adress?.quarter ?: "") }
    var complement by remember { mutableStateOf(user?.adress?.complement ?: "") }

    // Senha (Obrigatória pelo Backend para atualizar)
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meu Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Voltar") }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Foto de Perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = PazWhite, modifier = Modifier.size(60.dp))
            }
            Text("Alterar Foto", color = PazBlack, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(8.dp))

            if (message != null) {
                Text(message!!, color = if(message!!.startsWith("Erro")) Color.Red else Color(0xFF006400), fontWeight = FontWeight.Bold)
            }

            // Campos
            SectionTitle("Dados Pessoais")
            PazInput("Nome Completo", name, { name = it }, icon = Icons.Default.Person)
            PazInput("Email", email, { email = it }, icon = Icons.Default.Email) // Email pode ser editado? Depende do backend

            // Campo de Senha (Novo)
            PazInput("Senha (Confirme para salvar) *", password, { password = it }, icon = Icons.Default.Lock, isPassword = true)
            Text("Digite sua senha atual (ou uma nova) para confirmar as alterações.", fontSize = 12.sp, color = Color.Gray)

            SectionTitle("Contato")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(0.3f)) { PazInput("DDD", ddd, { ddd = it }, isNumber = true, maxLength = 2) }
                Box(Modifier.weight(0.7f)) { PazInput("Celular", phone, { phone = it }, icon = Icons.Default.Phone, isNumber = true, maxLength = 9) }
            }

            SectionTitle("Endereço")
            PazInput("CEP", cep, { cep = it }, icon = Icons.Default.Place, isNumber = true, maxLength = 8, visualTransformation = CepTransformation())
            PazInput("Rua", street, { street = it }, icon = Icons.Default.Home)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(0.4f)) { PazInput("Número", number, { number = it }, isNumber = true) }
                Box(Modifier.weight(0.6f)) { PazInput("Bairro", quarter, { quarter = it }) }
            }
            PazInput("Complemento", complement, { complement = it })

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password.length >= 6 && user != null) {
                        isLoading = true
                        message = null
                        coroutineScope.launch {
                            val addressObj = Address(street, cep, quarter, number, complement)
                            val phones = listOf(Cellphone("+55", ddd, null, phone, null))

                            // Mantém a data de nascimento original pois não estamos editando aqui
                            // Se o backend aceitar string ISO, tentamos converter ou mandamos a original se possível
                            // Aqui vamos assumir que o usuário não muda a data de nascimento no perfil simplificado
                            val birthdayStr = user.birthday ?: "2000-01-01"

                            val request = UserCreateRequest(
                                name = name,
                                surname = user.surname, // Mantém original
                                email = email,
                                password = password, // Backend exige senha
                                birthday = birthdayStr,
                                urlImage = user.urlImage,
                                adress = addressObj,
                                cellphones = phones
                            )

                            if (ApiClient.updateUser(user.id, request)) {
                                message = "Perfil atualizado com sucesso!"
                            } else {
                                message = "Erro ao atualizar perfil."
                            }
                            isLoading = false
                        }
                    } else {
                        message = "Erro: A senha é obrigatória (min 6 caracteres)."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(25.dp),
                enabled = !isLoading
            ) {
                if(isLoading) CircularProgressIndicator(color = PazWhite)
                else Text("SALVAR ALTERAÇÕES", fontWeight = FontWeight.Bold)
            }
        }
    }
}