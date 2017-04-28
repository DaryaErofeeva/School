package com.bignerdranch.android.school.admin;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.bignerdranch.android.school.AccountFragment;
import com.bignerdranch.android.school.ContactsFragment;
import com.bignerdranch.android.school.OnFragmentListener;
import com.bignerdranch.android.school.PersonalInfoFragment;
import com.bignerdranch.android.school.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements OnFragmentListener, AsyncResponse {

    private FragmentManager mFragmentManager;
    private InputMethodManager mInputMethodManager;

    private HashMap<String, String> postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mFragmentManager = getSupportFragmentManager();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mFragmentManager
                .beginTransaction().
                replace(
                        R.id.ll_add_user_container,
                        new AccountFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(String login, String password) {
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData = new HashMap<>();
        postData.put("login", login);
        postData.put("password", password);

        mFragmentManager
                .beginTransaction().
                replace(
                        R.id.ll_add_user_container,
                        new PersonalInfoFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(String lastname, String firstname, String patronymic, String gender, Date birth_date) {
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData.put("lastname", lastname);
        postData.put("firstname", firstname);
        postData.put("patronymic", patronymic);
        postData.put("gender", gender);
        postData.put("birth_date", birth_date.toString());
        mFragmentManager
                .beginTransaction()
                .replace(
                        R.id.ll_add_user_container,
                        new ContactsFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(String email, List<String> phones) {
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData.put("email", email);

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, this);
        task.execute("http://i-gift.tech/create_user.php");
    }

    @Override
    public void processFinish(String s) {
        String msg = "Польователь " +
                postData.get("lastname") + " " +
                postData.get("firstname") + " " +
                postData.get("patronymic") + " ";
        if (s.contains("true"))
            msg += "добавлен";
        else msg += "не добавлен";

        Snackbar
                .make(
                        getCurrentFocus(),
                        msg,
                        Snackbar.LENGTH_LONG)
                .show();
    }
}
