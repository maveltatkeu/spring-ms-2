package com.javatechie.dto;


import java.time.LocalDate;

public class Order {

    private String orderId;
    private String items;
    private String status;
    private LocalDate date;

    public Order() {
    }
    public Order(String orderId, String items, String status, LocalDate date) {
        this.orderId = orderId;
        this.items = items;
        this.status = status;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", items='" + items + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
