package com.javorek.lab.orders.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)      // výchozí je EAGER → explicitně LAZY
    @JoinColumn(name = "order_id")
    private Order order;

    private String partNo;                  // odkaz na díl v Oracle (legacy)
    private int quantity;

    protected OrderItem() { }

    public OrderItem(String partNo, int quantity) {
        this.partNo = partNo;
        this.quantity = quantity;
    }

    void setOrder(Order order) { this.order = order; }
    public String getPartNo() { return partNo; }
    public int getQuantity() { return quantity; }
}
