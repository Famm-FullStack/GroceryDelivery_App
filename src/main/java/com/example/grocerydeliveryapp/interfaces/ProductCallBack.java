package com.example.grocerydeliveryapp.interfaces;


import com.example.grocerydeliveryapp.model.Product;

import java.util.ArrayList;

public interface ProductCallBack {

    void onComplete(ArrayList<Product> products);

}
