package com.javorek.lab.orders.repository;

import com.javorek.lab.orders.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Oprava N+1 – varianta A: JPQL s JOIN FETCH
    @Query("select o from Order o join fetch o.items")
    List<Order> findAllWithItems();

    // Oprava N+1 – varianta B: EntityGraph (deklarativně, bez psaní JOINu)
    @EntityGraph(attributePaths = "items")
    @Query("select o from Order o")
    List<Order> findAllWithItemsGraph();
}
