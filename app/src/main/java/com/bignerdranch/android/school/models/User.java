package com.bignerdranch.android.school.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;
import java.sql.Date;

/**
 * Created by Ero on 13.04.2017.
 */

public class User implements Serializable {
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

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }*/
}
