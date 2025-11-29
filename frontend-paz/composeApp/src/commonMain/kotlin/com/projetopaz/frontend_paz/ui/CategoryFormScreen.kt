package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape // <--- O IMPORT QUE FALTAVA
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
fun CategoryFormScreen(
    categoryToEdit: Category? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    var description by remember { mutableStateOf(categoryToEdit?.description ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (categoryToEdit != null) "Editar Categoria" else "Nova Categoria", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PazInput("Nome da Categoria *", name, { name = it })
            PazInput("Descrição", description, { description = it })

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val cat = Category(id = categoryToEdit?.id ?: 0, name = name, description = description)
                            val success = if (categoryToEdit == null) ApiClient.createCategory(cat) else ApiClient.updateCategory(cat.id, cat)
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