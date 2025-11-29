package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.model.UserLogin
import com.projetopaz.frontend_paz.network.ApiClient
import com.projetopaz.frontend_paz.theme.PazBlack
import com.projetopaz.frontend_paz.theme.PazGrayBgEnd
import com.projetopaz.frontend_paz.theme.PazWhite
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit // <--- Novo parâmetro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = PazGrayBgEnd) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("PAZ", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = PazBlack)
            Text("Login do Sistema", fontSize = 18.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 32.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = PazWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().widthIn(max = 400.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (errorMessage != null) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                    }

                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = password, onValueChange = { password = it },
                        label = { Text("Senha") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Senha")
                            }
                        }
                    )

                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    val response = ApiClient.login(UserLogin(email, password))
                                    isLoading = false
                                    if (response != null) onLoginSuccess() else errorMessage = "Email ou senha inválidos."
                                }
                            } else {
                                errorMessage = "Preencha todos os campos."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PazBlack),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) CircularProgressIndicator(color = PazWhite, modifier = Modifier.size(24.dp))
                        else Text("ENTRAR", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTÃO DE CADASTRO ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Não tem uma conta? ", color = Color.Gray)
                ClickableText(
                    text = AnnotatedString("Cadastre-se"),
                    style = TextStyle(color = PazBlack, fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    onClick = { onNavigateToRegister() }
                )
            }
        }
    }
}