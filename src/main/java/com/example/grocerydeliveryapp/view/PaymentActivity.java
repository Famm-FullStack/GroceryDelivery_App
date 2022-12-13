package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.interfaces.OrderCallBack;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    private RadioButton cashRadioBtn;
    private Button placeOrderBtn;
    private RelativeLayout orderSuccessLayout;
    private ProgressBar progressBar;
    private boolean orderPlaced = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        cashRadioBtn = findViewById(R.id.cashRadioBtn);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);
        orderSuccessLayout = findViewById(R.id.orderSuccessLayout);
        progressBar = findViewById(R.id.progressBar_paymentAct);

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cashRadioBtn.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    FirestoreFirebase ff = new FirestoreFirebase(PaymentActivity.this);
                    ff.placeOrder(new OrderCallBack() {
                        @Override
                        public void onComplete(String orderId) {
                            if (orderId.length() > 0) {
                                TextView orderIdTxt = findViewById(R.id.orderIdTxt);
                                orderIdTxt.setText("Your Order ID is : " + orderId);
                                progressBar.setVisibility(View.GONE);
                                orderSuccessLayout.setVisibility(View.VISIBLE);
                                placeOrderBtn.setVisibility(View.GONE);
                                ff.emptyCart();
                                orderPlaced = true;
                            }
                        }
                    });


                } else {
                    Toast.makeText(PaymentActivity.this, "Please Select a Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!orderPlaced) {
            onBackPressed();
        } else {
            openMainActivity();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!orderPlaced) {
            super.onBackPressed();
        } else {
            openMainActivity();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}