package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.adapter.ProductRecyclerViewAdapter;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.interfaces.ProductCallBack;
import com.example.grocerydeliveryapp.model.Cart;
import com.example.grocerydeliveryapp.model.Product;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private int shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        shopID = Integer.parseInt(getIntent().getStringExtra("ShopID"));

        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirestoreFirebase ff = new FirestoreFirebase(this);

        ff.getProductFromFirebase(new ProductCallBack() {
            @Override
            public void onComplete(final ArrayList<Product> products) {
                if (products != null && products.size() > 0) {
                    ff.getProductsFromCart(new CartCallBack() {
                        @Override
                        public void onComplete(ArrayList<Cart> cartList) {
                            ProductRecyclerViewAdapter adapter =
                                    new ProductRecyclerViewAdapter(ProductListActivity.this,
                                            products, cartList,shopID);
                            productRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}