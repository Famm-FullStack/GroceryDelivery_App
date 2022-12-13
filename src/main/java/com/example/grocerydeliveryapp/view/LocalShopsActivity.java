package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.adapter.ShopsAdapter;
import com.example.grocerydeliveryapp.model.LocalShop;

import java.util.ArrayList;
import java.util.List;

public class LocalShopsActivity extends AppCompatActivity {

    private List<LocalShop> shops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_shops);

        getSupportActionBar().setTitle("Local Shops");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        shops = new ArrayList<>();
        shops.add(new LocalShop(1, "Aldi", R.drawable.aldi));
        shops.add(new LocalShop(2, "Amazon Fresh", R.drawable.amazon_fresh));
        shops.add(new LocalShop(3, "Budgens", R.drawable.budgens));
        shops.add(new LocalShop(4, "Co-op Food", R.drawable.co_op_food));
        shops.add(new LocalShop(5, "Farmfoods", R.drawable.farm_foods));
        shops.add(new LocalShop(6, "Fulton's Foods", R.drawable.fulton_foods));
        shops.add(new LocalShop(7, "Heron Foods", R.drawable.heron_foods));
        shops.add(new LocalShop(8, "Iceland", R.drawable.iceland));
        shops.add(new LocalShop(9, "Marks & Spencer", R.drawable.marks_and_spencer));
        shops.add(new LocalShop(10, "Sainsbury's", R.drawable.sainsbury));

        RecyclerView shopsRV = findViewById(R.id.localShopsRV);
        shopsRV.setLayoutManager(new LinearLayoutManager(this));

        ShopsAdapter adapter = new ShopsAdapter(this, shops);
        shopsRV.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}