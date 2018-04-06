package com.example.nauma.restaurantadvisorapp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigRetrofit {
    private static final String Url = "http://10.188.182.218:8000/";

    public RestaurantApi configureRetrofit(String header)
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (!header.isEmpty()) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Aut", "Your-App-Name")
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                });
            };
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return  retrofit.create(RestaurantApi.class);
    }
}
