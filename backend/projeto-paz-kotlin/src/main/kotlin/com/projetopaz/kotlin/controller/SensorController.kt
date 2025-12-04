package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.service.SensorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sensor")
class SensorController(
    private val sensorService: SensorService
) {
    @GetMapping("/current")
    fun getCurrent(): ResponseEntity<Any> {
        return ResponseEntity.ok(sensorService.currentData)
    }
}