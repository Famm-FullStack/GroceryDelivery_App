package com.example.grocerydeliveryapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class SupermarketProductsFragment extends Fragment {


    private RecyclerView superMarketProductsRV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supermarket_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        String shopID = bundle.getString("ShopID");

        ((SupermarketActivity)getContext()).getSupportActionBar().setTitle("Supermarket Products");

        superMarketProductsRV = getView().findViewById(R.id.superMarketProductsRV);
        superMarketProductsRV.setHasFixedSize(true);
        superMarketProductsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<ProductList> call = RetrofitClient.getInstance().getMyApi().getProducts("d49b662edd55431cba6806562d47c32b", "a", "50");

        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.isSuccessful()) {

                    FirestoreFirebase ff = new FirestoreFirebase(getContext());
                    ff.getProductsFromCart(new CartCallBack() {
                        @Override
                        public void onComplete(ArrayList<Cart> cartList) {
                            ProductRecyclerViewAdapter adapter =
                                    new ProductRecyclerViewAdapter(getContext(),
                                            response.body().getProducts(), cartList, Integer.parseInt(shopID));
                            superMarketProductsRV.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Log.i("SuperMarketCall", t.getMessage());
            }
        });
    }

}