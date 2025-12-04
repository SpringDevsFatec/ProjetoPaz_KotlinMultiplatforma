package com.projetopaz.kotlin.config

import com.projetopaz.kotlin.service.SensorService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

@Configuration
class MqttConfig(
    private val sensorService: SensorService
) {

    @Bean
    fun mqttInputChannel(): MessageChannel = DirectChannel()

    @Bean
    fun inbound(): MessageProducer {
        // Conecta no Mosquitto local (o mesmo que o ESP32 está mandando)
        val adapter = MqttPahoMessageDrivenChannelAdapter(
            "tcp://localhost:1883",
            "springBootServer",
            "sensor/dht11"
        )
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.outputChannel = mqttInputChannel()
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    fun handler(): MessageHandler {
        return MessageHandler { message ->
            val payload = message.payload.toString()
            // Manda para o serviço atualizar a variável
            sensorService.updateFromMqtt(payload)
        }
    }
}