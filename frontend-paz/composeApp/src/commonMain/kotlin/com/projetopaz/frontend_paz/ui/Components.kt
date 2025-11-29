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