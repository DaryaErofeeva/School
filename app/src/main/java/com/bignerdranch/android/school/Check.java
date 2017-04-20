package com.bignerdranch.android.school;

import android.text.TextUtils;

/**
 * Created by Ero on 14.04.2017.
 */

public class Check {

    public static boolean isLoginValid(String login) {
        return login.matches("([A-Za-z0-9]+)");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("([A-Za-z0-9]+)");
    }

    public static boolean isNameValid(String name) {
        return name.matches("([А-ЯЁа-яё]+)");
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean isPhoneValid(String phone) {
        return phone.matches("^\\d{10,12}$");
    }
}
