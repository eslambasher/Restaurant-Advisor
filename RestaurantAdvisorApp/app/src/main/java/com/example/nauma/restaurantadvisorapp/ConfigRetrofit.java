package com.example.nauma.restaurantadvisorapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigRetrofit {
    public RestaurantApi configureRetrofit()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.188.182.218:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return  retrofit.create(RestaurantApi.class);
    }
}
