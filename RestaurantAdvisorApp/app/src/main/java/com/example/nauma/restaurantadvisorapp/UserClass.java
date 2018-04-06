package com.example.nauma.restaurantadvisorapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserClass {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("password_confirmation")
    @Expose
    private String password_confirmation;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("age")
    @Expose
    private String age;


    UserClass(String email, String password, String password_confirmation, String username, String name, String lastName, String age) {
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.username = username;
        this.name = name;
        this.age = age;
        this.lastName = lastName;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
