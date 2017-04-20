package com.bignerdranch.android.school;

import java.sql.Date;
import java.util.List;

/**
 * Created by Ero on 16.04.2017.
 */

public interface OnFragmentListener {
    void onFragmentInteraction(String login, String password);

    void onFragmentInteraction(String lastname, String firstname, String patronymic, String gender, Date birth_date);

    void onFragmentInteraction(String email, List<String> phones);
}
