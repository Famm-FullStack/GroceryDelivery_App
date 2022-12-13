package com.example.grocerydeliveryapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.grocerydeliveryapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.cartMenu:
                intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.nearBySupermarket:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                mAuth.signOut();

                if (mAuth.getCurrentUser() == null) {
                    intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Logging out failed!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showShops_OnClick(View view) {
        Intent intent = new Intent(MainActivity.this, LocalShopsActivity.class);
        startActivity(intent);
    }

    public void showSupermarket_OnClick(View view) {
        Intent intent = new Intent(MainActivity.this, SupermarketActivity.class);
        startActivity(intent);
    }
}