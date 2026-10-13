package com.javorek.lab.orders.web;

import com.javorek.lab.orders.dto.OrderDto;
import com.javorek.lab.orders.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/n-plus-one")
    public List<OrderDto> nPlusOne() { return orderService.findAllNPlusOne(); }

    @GetMapping("/join-fetch")
    public List<OrderDto> joinFetch() { return orderService.findAllJoinFetch(); }

    @GetMapping("/entity-graph")
    public List<OrderDto> entityGraph() { return orderService.findAllEntityGraph(); }
}
