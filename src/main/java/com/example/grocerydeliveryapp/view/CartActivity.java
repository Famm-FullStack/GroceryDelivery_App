package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.adapter.CartAdapter;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.model.Cart;
import com.example.grocerydeliveryapp.model.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private FirestoreFirebase ff;
    private RecyclerView cartItemRecyclerView;
    private static LinearLayout billAmountLayout, emptyCartLayout;
    private static TextView cartItemPrice, cartItemDiscount, cartItemDeliveryFee, cartItemTotalAmount;

    public static LinearLayout cartLayout;

    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        billAmountLayout = findViewById(R.id.billAmountLayout);
        cartItemPrice = findViewById(R.id.cartItemPrice);
        cartItemDiscount = findViewById(R.id.cartItemDiscount);
        cartItemDeliveryFee = findViewById(R.id.cartItemDeliveryFee);
        cartItemTotalAmount = findViewById(R.id.cartItemTotalAmount);
        cartItemRecyclerView = findViewById(R.id.productListRecyclerView);
        cartLayout = findViewById(R.id.cartLayout);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);


        Button checkoutBtn = findViewById(R.id.chekoutBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckoutActivity();
            }
        });

        cartLayout.setVisibility(View.GONE);

        cartItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemRecyclerView.setHasFixedSize(true);

        setCartItems();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setCartItems() {
        ff = new FirestoreFirebase(this);
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                if (cartList.size() > 0) {
                    CartAdapter adapter = new CartAdapter(CartActivity.this, cartList);
                    cartItemRecyclerView.setAdapter(adapter);
                    cartLayout.setVisibility(View.VISIBLE);
                } else {
                    cartLayout.setVisibility(View.GONE);
                    emptyCartLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public static void updateBillingDetails(ArrayList<Cart> cartItems) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (cartItems.size() > 0) {
            double itemPriceTotal = 0;
            double discount;
            double totalPayable;
            for (Cart cart : cartItems) {
                itemPriceTotal += (cart.getQuantity() * cart.getProduct().getPrice());
            }
            discount = (itemPriceTotal * (10 / 100));
            totalPayable = itemPriceTotal - discount;
            cartItemPrice.setText(df.format(itemPriceTotal));
            cartItemDiscount.setText(df.format(discount));
            cartItemDeliveryFee.setText("0");
            cartItemTotalAmount.setText(df.format(totalPayable));
            cartLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);
        } else {
            cartLayout.setVisibility(View.GONE);
            emptyCartLayout.setVisibility(View.VISIBLE);
        }

    }

    public void startCheckoutActivity() {
        Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
        startActivity(checkoutIntent);
    }
}