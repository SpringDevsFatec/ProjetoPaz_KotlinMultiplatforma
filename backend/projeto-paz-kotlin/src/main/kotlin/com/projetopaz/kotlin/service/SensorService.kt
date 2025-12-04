package com.projetopaz.kotlin.service

import org.springframework.stereotype.Service
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class SensorData(
    val temperature: Float = 0f,
    val humidity: Float = 0f
)

@Service
class SensorService {
    var currentData = SensorData()

    data class Esp32Payload(val temp: Float, val umid: Float)

    fun updateFromMqtt(jsonPayload: String) {
        try {
            val mapper = jacksonObjectMapper()
            val dados: Esp32Payload = mapper.readValue(jsonPayload)

            // Atualiza o estado atual convertendo para o formato do nosso sistema
            currentData = SensorData(
                temperature = dados.temp,
                humidity = dados.umid
            )
            println("🌡️ Sensor Atualizado: $currentData")
        } catch (e: Exception) {
            println("Erro ao ler JSON do MQTT: ${e.message}")
        }
    }
}