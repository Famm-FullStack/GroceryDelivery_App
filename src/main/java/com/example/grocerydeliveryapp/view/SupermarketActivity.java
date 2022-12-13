package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.adapter.ProductRecyclerViewAdapter;
import com.example.grocerydeliveryapp.api.ProductList;
import com.example.grocerydeliveryapp.api.RetrofitClient;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.model.Cart;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupermarketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);

        getSupportActionBar().setTitle("Supermarkets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SupermarketFragment()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showSuperMarketProducts(String shopID) {
        Bundle bundle = new Bundle();
        bundle.putString("ShopID", shopID);
        SupermarketProductsFragment fragment = new SupermarketProductsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}