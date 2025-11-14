package com.projetopaz.kotlin.controller

import com.projetopaz.kotlin.dto.OrderDTO
import com.projetopaz.kotlin.mapper.OrderMapper
import com.projetopaz.kotlin.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping("/{idSale}")
    fun createOrder(@PathVariable idSale: Long, @RequestBody dto: OrderDTO): ResponseEntity<OrderDTO> {
        val order = orderService.createOrder(idSale, dto) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(OrderMapper.toDTO(order))
    }

    @GetMapping("/sale-id/{idSale}")
    fun getOrdersBySale(@PathVariable idSale: Long): ResponseEntity<List<OrderDTO>> =
        ResponseEntity.ok(orderService.getBySaleId(idSale).map { OrderMapper.toDTO(it) })

    @GetMapping("/with-items/{idOrder}")
    fun getOrderWithItems(@PathVariable idOrder: Long): ResponseEntity<OrderDTO> {
        val order = orderService.getOrderWithItems(idOrder) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(OrderMapper.toDTO(order))
    }

    @DeleteMapping("/cancelled/{idOrder}")
    fun cancel(@PathVariable idOrder: Long): ResponseEntity<String> =
        if (orderService.cancelOrder(idOrder)) ResponseEntity.ok("Pedido cancelado com sucesso")
        else ResponseEntity.notFound().build()
}
