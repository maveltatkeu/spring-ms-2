package com.javatechie.dto;



public class Shipping {
    private String orderId;
    private String status;
    private String currentLocation;
    private String eta; // Estimated Time of Arrival

    public Shipping() {
    }

    public Shipping(String orderId, String status, String currentLocation, String eta) {
        this.orderId = orderId;
        this.status = status;
        this.currentLocation = currentLocation;
        this.eta = eta;
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

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    @Override
    public String toString() {
        return "Shipping{" +
                "orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", eta='" + eta + '\'' +
                '}';
    }
}