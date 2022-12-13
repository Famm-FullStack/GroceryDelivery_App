package com.example.grocerydeliveryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.view.ProductListActivity;
import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.model.LocalShop;

import java.util.List;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopViewHolder> {

    private Context mContext;
    private List<LocalShop> shops;

    public ShopsAdapter(Context context, List<LocalShop> shops) {
        this.mContext = context;
        this.shops = shops;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_list_item, parent, false);

        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        LocalShop shop = shops.get(position);

        holder.shopImageIV.setImageResource(shop.getImage());
        holder.shopNameTV.setText(shop.getName());
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        private ImageView shopImageIV;
        private TextView shopNameTV;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            shopImageIV = itemView.findViewById(R.id.shopImageIV);
            shopNameTV = itemView.findViewById(R.id.shopNameTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductListActivity.class);
                    intent.putExtra("ShopID", String.valueOf(shops.get(getAdapterPosition()).getID()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
