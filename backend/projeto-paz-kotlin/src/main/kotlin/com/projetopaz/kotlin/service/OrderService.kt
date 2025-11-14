package com.projetopaz.kotlin.service

import com.projetopaz.kotlin.dto.OrderDTO
import com.projetopaz.kotlin.mapper.ItemOrderMapper
import com.projetopaz.kotlin.mapper.OrderMapper
import com.projetopaz.kotlin.model.Order
import com.projetopaz.kotlin.repository.ItemOrderRepository
import com.projetopaz.kotlin.repository.OrderRepository
import com.projetopaz.kotlin.repository.SaleRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val saleRepository: SaleRepository,
    private val itemOrderRepository: ItemOrderRepository
) {

    fun createOrder(saleId: Long, dto: OrderDTO): Order? {
        val sale = saleRepository.findById(saleId).orElse(null) ?: return null
        if (sale.status != 1) return null
        // cria order
        val order = OrderMapper.toEntity(dto)
        order.sale = sale

        // ⭐ Calcula o total_amount diretamente da lista enviada no DTO
        val totalAmount = dto.items.sumOf { it.unitPrice }
        order.total_amount_order  = totalAmount

        val savedOrder = orderRepository.save(order)

        // cria itens
        val items = dto.items.map {
            val entity = ItemOrderMapper.toEntity(it)
            entity.order = savedOrder
            itemOrderRepository.save(entity)
        }

        // adiciona itens na entity já persistida
        savedOrder.items = items.toMutableSet()

        return savedOrder
    }


    fun getBySaleId(saleId: Long): List<Order> =
        orderRepository.findAllBySaleId(saleId)

    fun getOrderWithItems(idOrder: Long): Order? =
        orderRepository.findById(idOrder).orElse(null)?.takeIf { it.status }

    fun cancelOrder(idOrder: Long): Boolean {
        val order = orderRepository.findById(idOrder).orElse(null) ?: return false
        order.status = false
        orderRepository.save(order)
        return true
    }
}


