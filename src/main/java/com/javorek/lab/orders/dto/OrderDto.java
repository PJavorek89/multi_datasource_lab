package com.javorek.lab.orders.dto;

import com.javorek.lab.orders.model.Order;

import java.time.LocalDate;
import java.util.List;

public record OrderDto(Long id, String customer, LocalDate createdAt, List<OrderItemDto> items) {

    public static OrderDto from(Order o) {
        return new OrderDto(o.getId(), o.getCustomer(), o.getCreatedAt(),
                o.getItems().stream()                                  // ← zde se sahá na LAZY kolekci
                        .map(i -> new OrderItemDto(i.getPartNo(), i.getQuantity()))
                        .toList());
    }
}
