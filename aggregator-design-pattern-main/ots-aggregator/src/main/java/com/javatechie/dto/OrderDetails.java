package com.javatechie.dto;

public class OrderDetails {

    private Order order;

    private Payment payment;

    private Shipping shipping;

    public OrderDetails() {
    }

    public OrderDetails(Order order, Payment payment, Shipping shipping) {
        this.order = order;
        this.payment = payment;
        this.shipping = shipping;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "order=" + order +
                ", payment=" + payment +
                ", shipping=" + shipping +
                '}';
    }
}
