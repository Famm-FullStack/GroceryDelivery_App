package com.example.grocerydeliveryapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.interfaces.AddressCallBack;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.model.Address;
import com.example.grocerydeliveryapp.model.Cart;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private FirestoreFirebase ff;
    private Button proceedBtn;
    private static TextView cartItemPrice, cartItemDiscount, cartItemDeliveryFee, cartItemTotalAmount;
    private RelativeLayout checkoutLayout;
    private ProgressBar checkoutProgressBar;

    private LinearLayout addressLayout;
    private TextView addressTxt;
    private Button addressBtn, changeAddressBtn;

    BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout addressBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        proceedBtn = findViewById(R.id.proceedBtn);
        cartItemPrice = findViewById(R.id.cartItemPrice);
        cartItemDiscount = findViewById(R.id.cartItemDiscount);
        cartItemDeliveryFee = findViewById(R.id.cartItemDeliveryFee);
        cartItemTotalAmount = findViewById(R.id.cartItemTotalAmount);
        checkoutLayout = findViewById(R.id.checkoutLayout);
        checkoutProgressBar = findViewById(R.id.checkoutProgressBar);

        addressLayout = findViewById(R.id.addressLayout);
        addressTxt = findViewById(R.id.addressTxt);
        changeAddressBtn = findViewById(R.id.changeAddressBtn);
        addressBtn = findViewById(R.id.addAddressBtn);
        addressLayout.setVisibility(View.GONE);
        addressBottomSheet = findViewById(R.id.addressBottomSheet);


        checkoutLayout.setVisibility(View.GONE);

        updateBillingDetails();
        setAddress(0);
        setBottomSheetBehavior();


        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressintent = new Intent(CheckoutActivity.this, AddressActivity.class);
                startActivity(addressintent);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentActivity();
            }
        });

        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setAddress(final int selectedAddress) {
        ff.getAddressFromFirebase(new AddressCallBack() {
            @Override
            public void onComplete(ArrayList<Address> addresses) {
                if (addresses.size() > 0) {
                    addressTxt.setText(addresses.get(selectedAddress).toString());
                    addressLayout.setVisibility(View.VISIBLE);
                    addressBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void startPaymentActivity() {
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentIntent);
        finish();
    }

    private void updateBillingDetails() {
        ff = new FirestoreFirebase(this);
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                DecimalFormat df = new DecimalFormat("#.##");
                double itemPriceTotal = 0;
                double discount;
                double totalPayable;
                for (Cart cart : cartList) {
                    itemPriceTotal = itemPriceTotal + (cart.getQuantity() * cart.getProduct().getPrice());
                }
                discount = (itemPriceTotal * (10 / 100));
                totalPayable = itemPriceTotal - discount;
                cartItemPrice.setText(df.format(itemPriceTotal));
                cartItemDiscount.setText(df.format(discount));
                cartItemDeliveryFee.setText("0");
                cartItemTotalAmount.setText(df.format(totalPayable));
                checkoutProgressBar.setVisibility(View.GONE);
                checkoutLayout.setVisibility(View.VISIBLE);

            }
        });


    }

    private void setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(addressBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        showBottomSheet();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public void showBottomSheet() {
        addressBottomSheet.setVisibility(View.VISIBLE);
        AddressBottomSheetDialogFragment addressBottomSheetDialogFragment = new AddressBottomSheetDialogFragment();
        addressBottomSheetDialogFragment.show(getSupportFragmentManager(), "AddressBottomSheetDialogFragment");
    }
}