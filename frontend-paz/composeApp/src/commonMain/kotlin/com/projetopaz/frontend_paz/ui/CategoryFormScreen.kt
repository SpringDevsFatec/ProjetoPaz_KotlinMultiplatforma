package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.projetopaz.frontend_paz.model.Category
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun CategoryFormScreen(
    categoryToEdit: Category? = null,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // --- ESTADOS (Sem descrição) ---
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    var isFavorite by remember { mutableStateOf(categoryToEdit?.favorite ?: false) }

    // Tipo da Categoria
    var categoryType by remember { mutableStateOf(categoryToEdit?.categoryType ?: "PRODUTO") }
    var expandedType by remember { mutableStateOf(false) }
    val types = listOf("PRODUTO", "SERVIÇO", "OUTRO")

    // Imagem
    var imgBase64 by remember { mutableStateOf<String?>(null) }
    var hasImageSelected by remember { mutableStateOf(false) }

    // --- CONFIGURAÇÃO DA GALERIA ---
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecione a imagem"
    ) { file ->
        if (file != null) {
            coroutineScope.launch {
                val bytes = file.readBytes()
                imgBase64 = Base64.encode(bytes)
                hasImageSelected = true
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (categoryToEdit != null) "Editar Categoria" else "Nova Categoria", fontWeight = FontWeight.Bold)
                },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. Campo NOME
            Text("Nome", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PazWhite,
                    unfocusedContainerColor = PazWhite
                )
            )

            // 2. Campo TIPO
            Text("Tipo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = categoryType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expandedType = !expandedType }) {
                            Icon(if (expandedType) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown, null)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PazWhite,
                        unfocusedContainerColor = PazWhite
                    )
                )
                DropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    types.forEach { type ->
                        DropdownMenuItem(text = { Text(type) }, onClick = { categoryType = type; expandedType = false })
                    }
                }
            }

            // 3. Campo IMAGEM (Ajustado para mostrar inteira)
            Text("Imagem", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Um pouco mais alto para caber bem
                    .clip(RoundedCornerShape(12.dp))
                    .background(PazWhite)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .clickable { launcher.launch() },
                contentAlignment = Alignment.Center
            ) {
                if (categoryToEdit?.imgCategory != null && imgBase64 == null) {
                    // IMAGEM DO BANCO (URL)
                    AsyncImage(
                        model = categoryToEdit.imgCategory,
                        contentDescription = "Imagem Categoria",
                        modifier = Modifier.fillMaxSize(),
                        // USE 'Fit' para ver a imagem inteira sem cortes
                        contentScale = ContentScale.Fit
                    )
                } else if (hasImageSelected) {
                    // FEEDBACK VISUAL (Nova imagem)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(64.dp))
                        Text("Imagem Pronta!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        Text("Salve para visualizar", fontSize = 12.sp, color = Color.Gray)
                    }
                } else {
                    // PLACEHOLDER
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                        Text("Toque para adicionar foto", color = Color.Gray)
                    }
                }

                // Botão Favorito
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorito",
                        modifier = Modifier.size(32.dp).clickable { isFavorite = !isFavorite },
                        tint = if (isFavorite) Color(0xFFFFD700) else Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // BOTÃO SALVAR (Sem enviar descrição)
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val cat = Category(
                                id = categoryToEdit?.id ?: 0,
                                name = name,
                                description = null, // Envia nulo ou vazio
                                categoryType = categoryType,
                                favorite = isFavorite,
                                imgBase64 = imgBase64,
                                imgCategory = categoryToEdit?.imgCategory
                            )

                            val success = if (categoryToEdit == null)
                                ApiClient.createCategory(cat)
                            else
                                ApiClient.updateCategory(cat.id, cat)

                            if (success) onSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (categoryToEdit != null) "SALVAR ALTERAÇÕES" else "CADASTRAR CATEGORIA", fontWeight = FontWeight.Bold)
            }
        }
    }
}