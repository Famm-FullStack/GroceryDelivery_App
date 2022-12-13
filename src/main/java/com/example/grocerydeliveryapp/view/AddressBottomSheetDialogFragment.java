package com.example.grocerydeliveryapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.adapter.AddressAdapter;
import com.example.grocerydeliveryapp.interfaces.AddressCallBack;
import com.example.grocerydeliveryapp.model.Address;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class AddressBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private TextView addNewAddressBtn, closeBtn;
    private RecyclerView addressRecyclerView;
    private Button selectAddressBtn;
    private int selectedAddress = 0;
    private AddressAdapter adapter;
    private ProgressBar addressProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.address_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressRecyclerView = getView().findViewById(R.id.addressRecyclerView);
        addNewAddressBtn = getView().findViewById(R.id.addNewAddress);
        closeBtn = getView().findViewById(R.id.closeBtn);
        selectAddressBtn = getView().findViewById(R.id.selectAddressBtn);
        addressProgressBar = getView().findViewById(R.id.addressProgressBar);


        addressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addressRecyclerView.setHasFixedSize(true);
        FirestoreFirebase ff = new FirestoreFirebase(getContext());
        ff.getAddressFromFirebase(new AddressCallBack() {
            @Override
            public void onComplete(ArrayList<Address> addresses) {
                adapter = new AddressAdapter(getContext(), addresses);
                addressRecyclerView.setAdapter(adapter);
                addressProgressBar.setVisibility(View.GONE);
            }
        });

        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressintent = new Intent(getContext(), AddressActivity.class);
                startActivity(addressintent);
                getActivity().finish();
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        selectAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    selectedAddress = adapter.selectedAddress;
                    ((CheckoutActivity) getActivity()).setAddress(selectedAddress);
                    getDialog().dismiss();
                }
            }
        });

    }
}