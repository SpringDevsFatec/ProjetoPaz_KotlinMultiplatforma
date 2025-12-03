package com.projetopaz.frontend_paz.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projetopaz.frontend_paz.theme.PazBlack
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.projetopaz.frontend_paz.model.SaleResponse
import com.projetopaz.frontend_paz.theme.PazWhite
import com.projetopaz.frontend_paz.theme.PazBlack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

@Composable
fun PazInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    isNumber: Boolean = false,
    isPassword: Boolean = false,
    placeholder: String = "",
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.Black
        )
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else visualTransformation,
            singleLine = true,
            leadingIcon = if (icon != null) {
                { Icon(icon, contentDescription = null, tint = Color.Gray) }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEBEBEB),
                unfocusedContainerColor = Color(0xFFEBEBEB),
                focusedBorderColor = PazBlack,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}

@Composable
fun SaleHistoryCard(sale: SaleResponse, onClick: () -> Unit) {
    // Calcula total
    val total = sale.orders.sumOf { it.total }
    val itemCount = sale.orders.sumOf { it.items.size }

    // Formata Data
    val dateDisplay = sale.createdAt?.take(10)?.split("-")?.reversed()?.joinToString("/") ?: "--/--/--"

    val (statusText, statusColor) = when (sale.status) {
        1 -> "Ativa" to Color(0xFFFFA500) // Laranja
        2 -> "Concluída" to Color(0xFF4CAF50)      // Verde
        0 -> "Cancelada" to Color.Red              // Vermelho
        else -> "Status $sale.status" to Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = PazWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Venda #${sale.id}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text(dateDisplay, color = Color.Gray, fontSize = 12.sp)

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                    Spacer(Modifier.width(4.dp))
                    Text(statusText, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Bold)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("R$ $total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PazBlack)
                Text("$itemCount itens", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}