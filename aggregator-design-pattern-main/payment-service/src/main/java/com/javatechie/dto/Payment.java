package com.javatechie.dto;


public class Payment {
    private String orderId;
    private String status;
    private String method; // e.g., Credit Card, PayPal, UPI

    public Payment() {
    }

    public Payment(String orderId, String status, String method) {
        this.orderId = orderId;
        this.status = status;
        this.method = method;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}