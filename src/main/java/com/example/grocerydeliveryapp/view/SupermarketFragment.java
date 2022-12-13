package com.example.grocerydeliveryapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.adapter.ShopsAdapter;
import com.example.grocerydeliveryapp.adapter.SupermarketsAdapter;
import com.example.grocerydeliveryapp.model.LocalShop;

import java.util.ArrayList;
import java.util.List;

public class SupermarketFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supermarket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<LocalShop> shops = new ArrayList<>();
        shops.add(new LocalShop(101, "Aldi", R.drawable.aldi));
        shops.add(new LocalShop(102, "Amazon Fresh", R.drawable.amazon_fresh));

        RecyclerView supermarketsRV = getView().findViewById(R.id.supermarketsRV);
        supermarketsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        SupermarketsAdapter adapter = new SupermarketsAdapter(getContext(), shops);
        supermarketsRV.setAdapter(adapter);
    }
}