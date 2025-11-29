package com.projetopaz.frontend_paz.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Remove tudo que não é dígito e limita a 8 caracteres (DDMMAAAA)
        val trimmed = text.text.filter { it.isDigit() }.take(8)

        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            // Adiciona barra após o dia (2 chars) e após o mês (4 chars)
            if (i == 1 || i == 3) output += "/"
        }

        val dateOffsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(output), dateOffsetMapping)
    }
}

class CepTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita a 8 dígitos
        val trimmed = text.text.filter { it.isDigit() }.take(8)

        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            // Adiciona o traço após o 5º dígito (índice 4)
            if (i == 4) output += "-"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 8) return offset + 1
                return 9
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 5) return offset
                if (offset <= 9) return offset - 1
                return 8
            }
        }

        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}

// Função utilitária para converter "23/03/2005" -> "2005-03-23" (Backend)
fun convertDateToBackendFormat(dateInput: String): String {
    val clean = dateInput.filter { it.isDigit() }
    if (clean.length != 8) return ""

    val day = clean.substring(0, 2)
    val month = clean.substring(2, 4)
    val year = clean.substring(4, 8)

    return "$year-$month-$day"
}