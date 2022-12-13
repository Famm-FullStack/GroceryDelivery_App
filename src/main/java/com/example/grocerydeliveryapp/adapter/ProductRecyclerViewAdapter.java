package com.example.grocerydeliveryapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.Utils.FirestoreFirebase;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.interfaces.DataAddedCallBack;
import com.example.grocerydeliveryapp.model.Cart;
import com.example.grocerydeliveryapp.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<Product> products;
    private ArrayList<Cart> cartArrayList;
    private FirestoreFirebase ff;
    private int shopID;

    public ProductRecyclerViewAdapter(Context context, List<Product> products, ArrayList<Cart> cartList, int shopID) {
        mContext = context;
        this.products = products;
        this.cartArrayList = cartList;
        this.shopID = shopID;
        ff = new FirestoreFirebase(mContext);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Product product = products.get(position);
        Picasso.get().load(product.getImage()).into(holder.itemImage);
        holder.itemName.setText(product.getTitle());
        product.setPrice(getRandomPrice());
        holder.itemPrice.setText(product.getPrice() + "$");


        if (cartArrayList.size() > 0) {
            Cart cart = null;
            for (Cart c : cartArrayList) {
                if (product.getId().equals(c.getProduct().getId()) && shopID == c.getShopID()) {
                    cart = c;
                    break;
                }
            }
            if (cart != null) {
                Log.i("ProductMatched", "ID " + product.getId() + " Cart " + cart.getProduct().getId());
                holder.itemAddToCartBtn.setVisibility(View.GONE);
                holder.itemIncrDecrLayout.setVisibility(View.VISIBLE);
                holder.itemWeight.setText(String.valueOf(cart.getQuantity()));
            } else {
                holder.itemAddToCartBtn.setVisibility(View.VISIBLE);
                holder.itemIncrDecrLayout.setVisibility(View.GONE);
                holder.itemWeight.setText("1");
            }
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemWeight;
        private TextView itemWeightDecrement;
        private TextView itemWeightIncrement;
        private RelativeLayout itemIncrDecrLayout;
        private TextView itemAddToCartBtn;
        private ProgressBar itemWeightProgressBar;
        private String incrementedWeight;
        private String decrementedWeight;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemprice);
            itemWeight = itemView.findViewById(R.id.itemWeight);
            itemWeightDecrement = itemView.findViewById(R.id.itemWeightDecrement);
            itemWeightIncrement = itemView.findViewById(R.id.itemWeightIncrement);
            itemIncrDecrLayout = itemView.findViewById(R.id.itemIncrDecrLayout);
            itemAddToCartBtn = itemView.findViewById(R.id.itemAddToCartBtn);
            itemWeightProgressBar = itemView.findViewById(R.id.itemWeightProgressBar);


            itemAddToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = products.get(getAdapterPosition());
                    ff.addProductToCart(null, product, "1", shopID, new DataAddedCallBack() {
                        @Override
                        public void onSuccess(boolean successful) {
                            if (successful) {
                                getProductFromCart();
                            } else {
                                Log.i("AddProductToCart", "Failed");
                            }
                        }

                        @Override
                        public void onFailure(boolean failed) {
                            Log.i("AddProductToCart", "Failed");
                        }
                    });
                }
            });

            itemWeightIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    Product product = products.get(getAdapterPosition());
                    for (Cart cart : cartArrayList) {
                        if (product.getId().equals(cart.getProduct().getId())) {
                            incrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) + 1);
                            ff.addProductToCart(cart.getCartId(), product, incrementedWeight, shopID, new DataAddedCallBack() {
                                @Override
                                public void onSuccess(boolean successful) {
                                    if (successful) {
                                        itemWeight.setText(incrementedWeight);
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(boolean failed) {
                                    itemWeightProgressBar.setVisibility(View.GONE);
                                }
                            });
                            break;
                        }
                    }
                }
            });

            itemWeightDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(itemWeight.getText().toString()) > 1) {
                        Product product = products.get(getAdapterPosition());
                        for (Cart cart : cartArrayList) {
                            if (product.getId().equals(cart.getProduct().getId())) {
                                decrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) - 1);
                                ff.addProductToCart(cart.getCartId(), product, decrementedWeight, shopID, new DataAddedCallBack() {
                                    @Override
                                    public void onSuccess(boolean successful) {
                                        itemWeight.setText(decrementedWeight);
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(boolean failed) {
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            }
                        }
                    } else {
                        Product product = products.get(getAdapterPosition());
                        for (Cart cart : cartArrayList) {
                            if (product.getId() == cart.getProduct().getId()) {
                                ff.removeProductFromCart(cart.getCartId());
                                itemAddToCartBtn.setVisibility(View.VISIBLE);
                                itemIncrDecrLayout.setVisibility(View.GONE);
                                itemWeightProgressBar.setVisibility(View.GONE);
                                break;
                            }
                        }

                    }
                }
            });
        }

        private void getProductFromCart() {
            ff.getProductsFromCart(new CartCallBack() {
                @Override
                public void onComplete(ArrayList<Cart> cartList) {
                    cartArrayList = cartList;
                    itemAddToCartBtn.setVisibility(View.GONE);
                    itemIncrDecrLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public double getRandomPrice() {
        double val = (int) (Math.random() * (10 - 3 + 1) + 3);
        val += 0.99;
        return val;
    }
}
