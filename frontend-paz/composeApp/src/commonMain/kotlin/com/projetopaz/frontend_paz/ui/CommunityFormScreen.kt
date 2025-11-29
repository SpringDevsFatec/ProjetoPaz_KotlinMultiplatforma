package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape // <--- O IMPORT QUE FALTAVA
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
fun CommunityFormScreen(
    communityToEdit: Community? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf(communityToEdit?.name ?: "") }
    var description by remember { mutableStateOf(communityToEdit?.description ?: "") }
    var cep by remember { mutableStateOf(communityToEdit?.cep ?: "") }
    var quarter by remember { mutableStateOf(communityToEdit?.quarter ?: "") }
    var number by remember { mutableStateOf(communityToEdit?.number ?: "") }
    var complement by remember { mutableStateOf(communityToEdit?.complement ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (communityToEdit != null) "Editar Comunidade" else "Nova Comunidade", fontWeight = FontWeight.Bold) },
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
            PazInput("Nome da Comunidade *", name, { name = it })
            PazInput("Descrição *", description, { description = it })

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // CEP com máscara e limite
                Box(Modifier.weight(1f)) {
                    PazInput(
                        label = "CEP *",
                        value = cep,
                        onValueChange = { if (it.all { c -> c.isDigit() }) cep = it },
                        isNumber = true,
                        maxLength = 8,
                        visualTransformation = CepTransformation(),
                        placeholder = "00000-000"
                    )
                }
                Box(Modifier.weight(1f)) { PazInput("Número *", number, { number = it }, isNumber = true) }
            }

            PazInput("Bairro *", quarter, { quarter = it })
            PazInput("Complemento", complement, { complement = it })

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && cep.isNotBlank()) {
                        coroutineScope.launch {
                            val req = CommunityRequest(name, description, cep, quarter, number, complement)
                            val success = if (communityToEdit == null) ApiClient.createCommunity(req) else ApiClient.updateCommunity(communityToEdit.id, req)
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