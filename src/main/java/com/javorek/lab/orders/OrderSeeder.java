package com.javorek.lab.orders;

import com.javorek.lab.orders.model.Order;
import com.javorek.lab.orders.model.OrderItem;
import com.javorek.lab.orders.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderSeeder implements CommandLineRunner {   // spustí se po startu aplikace

    private final OrderRepository orderRepository;

    public OrderSeeder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (orderRepository.count() > 0) return;          // neplnit znovu při každém startu
        for (int i = 1; i <= 10; i++) {
            var order = new Order("Customer " + i, LocalDate.now().minusDays(i));
            order.addItem(new OrderItem("P%09d".formatted(i), 1));
            order.addItem(new OrderItem("P%09d".formatted(i + 100), 2));
            order.addItem(new OrderItem("P%09d".formatted(i + 200), 3));
            orderRepository.save(order);                  // cascade ALL uloží i položky
        }
    }
}
