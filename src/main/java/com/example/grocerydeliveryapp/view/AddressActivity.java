package com.example.grocerydeliveryapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.interfaces.DataAddedCallBack;
import com.example.grocerydeliveryapp.model.Address;

public class AddressActivity extends AppCompatActivity {

    private EditText nameEditTxt, addressEditTxt, pincodeEditTxt, cityEditTxt, stateEditTxt, mobileNoEditTxt, countryEditText;
    private Button saveAddressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getSupportActionBar().setTitle("Add New Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        nameEditTxt = findViewById(R.id.nameEditTxt);
        addressEditTxt = findViewById(R.id.addressEditTxt);
        pincodeEditTxt = findViewById(R.id.pincodeEditTxt);
        cityEditTxt = findViewById(R.id.cityEditTxt);
        stateEditTxt = findViewById(R.id.cityEditTxt);
        mobileNoEditTxt = findViewById(R.id.mobileNoEditTxt);
        saveAddressBtn = findViewById(R.id.saveAddressBtn);
        countryEditText = findViewById(R.id.countryEditText);

        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString().trim();
                String add = addressEditTxt.getText().toString().trim();
                String pincode = pincodeEditTxt.getText().toString().trim();
                String city = cityEditTxt.getText().toString().trim();
                String state = stateEditTxt.getText().toString().trim();
                String country = countryEditText.getText().toString().trim();
                String mobileNo = mobileNoEditTxt.getText().toString().trim();


                if (name.length() > 0) {
                    if (add.length() > 0) {
                        if (pincode.length() > 0) {
                            if (city.length() > 0) {
                                if (state.length() > 0) {
                                    if (country.length() > 0) {
                                        if (mobileNo.length() > 0) {
                                            Address address = new Address("0", name, add, pincode, city, state, country, mobileNo);
                                            FirestoreFirebase ff = new FirestoreFirebase(AddressActivity.this);
                                            ff.addAddressIntoFirebase(null, address, new DataAddedCallBack() {
                                                @Override
                                                public void onSuccess(boolean successful) {
                                                    Intent checkoutActivityintent = new Intent(AddressActivity.this, CheckoutActivity.class);
                                                    startActivity(checkoutActivityintent);
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(boolean failed) {

                                                }
                                            });
                                        } else {
                                            Toast.makeText(AddressActivity.this, "Mobile Number can not be empty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(AddressActivity.this, "Country can not be empty", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(AddressActivity.this, "State can not be empty", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddressActivity.this, "City can not be empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddressActivity.this, "Pincode can not be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddressActivity.this, "Address can not be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddressActivity.this, "Name can not be empty", Toast.LENGTH_SHORT).show();
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