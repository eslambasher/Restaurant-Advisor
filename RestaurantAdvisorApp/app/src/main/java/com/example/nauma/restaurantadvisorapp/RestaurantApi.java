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
            "Content-Type: application/json"
    })
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZlMmVlYWUzNDg5NDA2MWViYWMxODc5NWNhNzFjYzJhMGY2ZTFkYjQxMGEwM2I3ZTI4YmM3ZjU4NWViOTJmOGM3NjI5MDkxMjRkNDgxMjk5In0.eyJhdWQiOiIxIiwianRpIjoiNmUyZWVhZTM0ODk0MDYxZWJhYzE4Nzk1Y2E3MWNjMmEwZjZlMWRiNDEwYTAzYjdlMjhiYzdmNTg1ZWI5MmY4Yzc2MjkwOTEyNGQ0ODEyOTkiLCJpYXQiOjE1MjI3MDQ5NjEsIm5iZiI6MTUyMjcwNDk2MSwiZXhwIjoxNTU0MjQwOTYxLCJzdWIiOiIxMSIsInNjb3BlcyI6W119.ReIylO94YE_eeyQIMpIDTPK1pruyvdz4GUKSYfvp_OT8z2vW-Yg7iPydbsdGU6BVkDaqaqQca4DvMICLQf05NLsx9cVz0yvAd8NJzxj5gw08-LW_DRCorOkH4RKWzTHRPc7iYDK-EdWT1ckff0XrJJ6uDnoj2Rr9aAn6irt0cbpsU1j9PpHrB5MH3tSOXrjyl_Rj1gET_GcxTRP9UXw__HCBDysQHXxqTtA7tywuKIew1V9wpSoYyZKmTUlITonK7ZBpzYqEb3NZWUwkhbI5HxTUTNyT9kVxnsDT3nGZZe-r_H_pTs4Zn8qWGc_fO9_3vJxBp25MSEbz3zj3ISSsck5DfyFsNgxTZyqHqAP_fLUTcsbkLax4r69p2ewY9jZjKvWSyixFKtWJlTXchGvqcmV4mn0Fwez7Q65CyfptAV19ElGzma8M8mmHbMEzZ-02uxiJVhf4Ay18UADOcO-ebQjuoko4Qf3RGUp1ePsZF8WqAiTOvGegfDxVkt4FHgaZ3kZ6h8Cb6PiyXvF0gZjzsejkZ58x8jzS5tCALRdLpHSQ80q8sbeXUPAuqNKP-YBSbrRm5hjZFMgtZ-gH_utsFEq7ekzxEX23KAz97rkRiVmOCuvQ5CtIFJ8gGPGRmk8mZizYYmat34FmN4OoM5PPjVGk8pkmd3fmFCfVRt1jHqI"
    })
    @POST("restaurants")
    Call<String> addRestaurants(@Body Restaurant restaurant);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("menus/restaurants/{restaurantId}")
    Call<List<Menus>> getMenusById(@Path("restaurantId") String restaurantId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })
    @POST("users/login")
    Call<UserClass> loginUser(@Body UserClass user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })
    @POST("users/register")
    Call<UserClass> signUp(@Body UserClass user);

}
