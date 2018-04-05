package com.example.nauma.restaurantadvisorapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nauma on 29/03/2018.
 */

public interface RestaurantApi {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("restaurants/getReastaurantWithSort/{sortType}/{order}")
    Call<List<Restaurant>> getSortedRestaurants(@Path("sortType") String sortType, @Path("order") String order);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("restaurants/{restaurantId}")
    Call<Restaurant> getSingleRestaurants(@Path("restaurantId") String restaurantId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjM5MmUyMDgzMGQ2YzBjOGE1NWM1MzRjYjc2MzYwODVjOGQxMmQzMGY3NTQ1NjE0Y2JlOTI4ZjAwY2NkMWUwYTBhNjhiZmVkZWU2MjhkYTM5In0.eyJhdWQiOiIxIiwianRpIjoiMzkyZTIwODMwZDZjMGM4YTU1YzUzNGNiNzYzNjA4NWM4ZDEyZDMwZjc1NDU2MTRjYmU5MjhmMDBjY2QxZTBhMGE2OGJmZWRlZTYyOGRhMzkiLCJpYXQiOjE1MjI4NTQ5NTgsIm5iZiI6MTUyMjg1NDk1OCwiZXhwIjoxNTU0MzkwOTU4LCJzdWIiOiIxMSIsInNjb3BlcyI6W119.VZL_cenLiAIqOC9Kke4XPBL9tJoIwlbfUCv-OBMNWQhuKE9DfpLdqOJw5DwbMEYQ5lx4lU3O_owGjdixFfeaY1vN1pZR0gTL7q5y0hfwWSQhZFcFa0ny3x16KtQ4h7eXpu06Kl-BEL3cLQuuHSm-e_G0tb6jsWp7QDJo_-9kwghAh7IR320dAIvjUapxQW8X4kZBu6ThpAQNfZ8f34vycSREMwPZputU_XtOtBks8l2XKa4y2-Ri13hUbRncECU-Mm7CmibqtK2oLODvss1bSzTQpTO6ObsjfjpwazSTR3XVxze6HCB-n4R3ApDLN-zf6HUD5obqdqM-h3G3eqE4HqSawoBm0FTAhPKYvjILsUUOfNIWf0NADVLLgujo3gBVJNtDaiVg3eJq9_1crak-OA0gEYcL24_wwOeBJEAE57OipvY72RA9iQo71ldBA51ZpzwlWoBYotQqrw_t9ApoXQef7rfFJYfSB3iTbWHRCyzqql4vG_yUsAb5vbP5bP8AwjfRfe6N3s66SLMVekrK3S4FPs3rdCthq2Du0Si51muuziUAZLnM1ZObud0rpO-rM6eumJgK6DkKiXIZqgev0-tABG3PyBnIqD5kQ5ilt5pZOejxyY3vN1Gnuv2zpp5rKFhnNbpCURdgIU_C4-ILUjjb3FCrIMH3j4-mKYfLz54"
    })
    @POST("restaurants")
    Call<Restaurant> addRestaurants(@Body Restaurant restaurant);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("menus/restaurants/{restaurantId}")
    Call<List<Menus>> getMenusById(@Path("restaurantId") String restaurantId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("comments/restaurants/{restaurantId}")
    Call<List<Comments>> getCommentById(@Path("restaurantId") String restaurantId);
}
