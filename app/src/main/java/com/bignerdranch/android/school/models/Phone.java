package com.bignerdranch.android.school.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ero on 28.04.2017.
 */

public class Phone {
    @SerializedName("id")
    public int  id;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("phone")
    public String phone;
}
