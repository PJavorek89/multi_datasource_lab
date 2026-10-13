package com.javorek.lab.orders.service;

import com.javorek.lab.orders.dto.OrderDto;
import com.javorek.lab.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;   // Spring, NE jakarta.transaction

import java.util.List;

@Service
@Transactional(transactionManager = "ordersTransactionManager", readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDto> findAllNPlusOne() {          // ❌ 1 dotaz na orders + N dotazů na položky
        return orderRepository.findAll().stream().map(OrderDto::from).toList();
    }

    public List<OrderDto> findAllJoinFetch() {         // ✅ 1 dotaz s JOINem
        return orderRepository.findAllWithItems().stream().map(OrderDto::from).toList();
    }

    public List<OrderDto> findAllEntityGraph() {       // ✅ 1 dotaz s JOINem
        return orderRepository.findAllWithItemsGraph().stream().map(OrderDto::from).toList();
    }
}
