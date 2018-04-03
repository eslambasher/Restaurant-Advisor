package com.example.nauma.restaurantadvisorapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nauma on 29/03/2018.
 */

public class Restaurant implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("localisation")
    @Expose
    private String localisation;

    @SerializedName("num_tel")
    @Expose
    private String num_tel;

    @SerializedName("site_web")
    @Expose
    private String site_web;

    @SerializedName("timeOpen_week")
    @Expose
    private String timeOpen_week;

    @SerializedName("timeOpen_weekend")
    @Expose
    private String timeOpen_weekend;

    public Restaurant() { }

    public Restaurant(String name, String note, String id) {
        this.id = id;
        this.name = name;
        this.note = note;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getLocalisation()
    {
        return localisation;
    }

    public void setLocalisation(String localisation)
    {
        this.localisation = localisation;
    }

    public String getNum_tel() { return num_tel; }

    public void setNum_tel(String num_tel)
    {
        this.num_tel = num_tel;
    }

    public String getSite_web() { return site_web; }

    public void setSite_web(String site_web)
    {
        this.site_web = site_web;
    }

    public String getTimeOpen_week() { return timeOpen_week; }

    public void setTimeOpen_week(String timeOpen_week)
    {
        this.timeOpen_week = timeOpen_week;
    }

    public String getTimeOpen_weekend() { return timeOpen_weekend; }

    public void setTimeOpen_weekend(String timeOpen_weekend)
    {
        this.timeOpen_weekend = timeOpen_weekend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.note);
        dest.writeString(this.description);
        dest.writeString(this.localisation);
        dest.writeString(this.num_tel);
        dest.writeString(this.site_web);
        dest.writeString(this.timeOpen_week);
        dest.writeString(this.timeOpen_weekend);
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>()
    {
        public Restaurant createFromParcel(Parcel source)
        {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.note = in.readString();
        this.description = in.readString();
        this.localisation = in.readString();
        this.num_tel = in.readString();
        this.site_web = in.readString();
        this.timeOpen_week = in.readString();
        this.timeOpen_weekend = in.readString();
    }
}
