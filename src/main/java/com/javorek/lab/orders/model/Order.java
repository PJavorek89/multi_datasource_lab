package com.javorek.lab.orders.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")                    // "order" je v SQL rezervované slovo
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Postgres identity sloupec
    private Long id;

    private String customer;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();    // @OneToMany je výchozí LAZY

    protected Order() { }                                  // JPA vyžaduje bezparametrický konstruktor

    public Order(String customer, LocalDate createdAt) {
        this.customer = customer;
        this.createdAt = createdAt;
    }

    public void addItem(OrderItem item) {                  // udržuje obě strany vazby v souladu
        items.add(item);
        item.setOrder(this);
    }

    public Long getId() { return id; }
    public String getCustomer() { return customer; }
    public LocalDate getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }
}
