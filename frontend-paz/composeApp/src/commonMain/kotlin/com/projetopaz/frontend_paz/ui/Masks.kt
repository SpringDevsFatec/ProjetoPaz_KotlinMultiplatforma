package com.projetopaz.frontend_paz.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// --- UTILITÁRIO GENÉRICO PARA MÁSCARAS ---
class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        var textIndex = 0

        while (textIndex < text.text.length && maskIndex < mask.length) {
            if (mask[maskIndex] != '#') {
                out += mask[maskIndex]
                maskIndex++
            } else {
                out += text.text[textIndex]
                maskIndex++
                textIndex++
            }
        }

        return TransformedText(AnnotatedString(out), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var offsetTotal = 0
                var count = 0
                for (i in mask.indices) {
                    if (count == offset) return offsetTotal
                    if (mask[i] == '#') {
                        count++
                    }
                    offsetTotal++
                }
                return offsetTotal
            }

            override fun transformedToOriginal(offset: Int): Int {
                var offsetTotal = 0
                var count = 0
                for (i in mask.indices) {
                    if (offsetTotal == offset) return count
                    if (mask[i] == '#') {
                        count++
                    }
                    offsetTotal++
                }
                return count
            }
        })
    }
}

// --- MÁSCARAS ESPECÍFICAS ---

// Data: 00/00/0000
class DateTransformation : VisualTransformation {
    private val mask = MaskVisualTransformation("##/##/####")
    override fun filter(text: AnnotatedString) = mask.filter(text)
}

// CEP: 00000-000
class CepTransformation : VisualTransformation {
    private val mask = MaskVisualTransformation("#####-###")
    override fun filter(text: AnnotatedString) = mask.filter(text)
}

// Apenas Número Celular (9 dígitos): 00000-0000
class CellNumberTransformation : VisualTransformation {
    private val mask = MaskVisualTransformation("#####-####")
    override fun filter(text: AnnotatedString) = mask.filter(text)
}

// CNPJ: 00.000.000/0000-00
class CnpjTransformation : VisualTransformation {
    private val mask = MaskVisualTransformation("##.###.###/####-##")
    override fun filter(text: AnnotatedString) = mask.filter(text)
}

// --- FUNÇÕES UTILITÁRIAS DE DATA ---

// 1. Converte da TELA (25/12/2025) para o BACKEND (2025-12-25)
fun convertDateToBackendFormat(dateInput: String): String {
    val clean = dateInput.filter { it.isDigit() }
    if (clean.length != 8) return ""
    val day = clean.substring(0, 2)
    val month = clean.substring(2, 4)
    val year = clean.substring(4, 8)
    return "$year-$month-$day"
}

// 2. Converte do BACKEND (2025-12-25) para a TELA (25122025)
// Essa função limpa a formatação do banco para a máscara funcionar direito na edição
fun formatBackendDateToUi(date: String?): String {
    if (date.isNullOrBlank()) return ""

    // Tenta separar por traço (formato padrão yyyy-mm-dd)
    val parts = date.split("-")
    if (parts.size == 3) {
        val year = parts[0]
        val month = parts[1]
        val day = parts[2]
        // Retorna apenas números: dia + mês + ano
        return "$day$month$year"
    }

    // Se não estiver no formato esperado, retorna apenas números
    return date.filter { it.isDigit() }
}