package com.example.grocerydeliveryapp.interfaces;


import com.example.grocerydeliveryapp.model.Cart;

import java.util.ArrayList;

public interface CartCallBack {

    void onComplete(ArrayList<Cart> cartList);

}
