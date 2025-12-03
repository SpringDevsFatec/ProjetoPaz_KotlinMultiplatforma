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

        // Só permite pedido se a venda estiver aberta (status 1)
        if (sale.status != 1) return null

        // Cria order
        val order = OrderMapper.toEntity(dto)
        order.sale = sale

        val totalAmount = dto.items.sumOf { it.unitPrice * it.quantity }

        order.total_amount_order = totalAmount

        val savedOrder = orderRepository.save(order)

        // Cria itens no banco
        val items = dto.items.map {
            val entity = ItemOrderMapper.toEntity(it)
            entity.order = savedOrder
            itemOrderRepository.save(entity)
        }

        // Atualiza a lista de itens na memória
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