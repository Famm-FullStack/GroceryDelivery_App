package com.example.grocerydeliveryapp.model;

import java.util.List;

public class Order {

    private String orderID;
    private List<Cart> cartList;
    private String orderDate;
    private double totalAmount;

    public Order() {
    }

    public Order(String orderID, List<Cart> cartList, String orderDate, double totalAmount) {
        this.orderID = orderID;
        this.cartList = cartList;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
