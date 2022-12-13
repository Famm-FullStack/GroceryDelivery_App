package com.example.grocerydeliveryapp.model;

public class Cart {
    private String cartId;
    private Product product;
    private int quantity;
    private int shopID;

    public Cart() {
    }

    public Cart(String cartId, Product product, int quantity, int shopID) {
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
        this.shopID = shopID;
    }

    public String getCartId() {
        return cartId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
