package com.example.nauma.restaurantadvisorapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nauma on 29/03/2018.
 */

public class Comments {

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("restaurant_id")
    @Expose
    private String restaurant_id;

    @SerializedName("username")
    @Expose
    private String username;

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getRestaurant_id()
    {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) { this.restaurant_id = restaurant_id; }

    public String getUsername()
    {
        return username;
    }

}
