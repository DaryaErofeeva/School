package com.bignerdranch.android.school.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

/**
 * Created by Ero on 13.04.2017.
 */

public class User {
    @SerializedName("id")
    public int  id;

    @SerializedName("login")
    public String login;

    @SerializedName("password")
    public String password;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("lastname")
    public String lastname;

    @SerializedName("patronymic")
    public String patronymic;

    @SerializedName("gender")
    public String gender;

    @SerializedName("birth_date")
    public String birth_date;

    @SerializedName("email")
    public String email;

    @SerializedName("photo")
    public String photo;
}
