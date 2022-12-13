package com.example.grocerydeliveryapp.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "https://api.spoonacular.com/";

    @GET("food/products/search")
    Call<ProductList> getProducts(@Query("apiKey") String apiKey, @Query("query") String productName,@Query("number") String number);
}