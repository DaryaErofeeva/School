package com.bignerdranch.android.school;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentListener {

    private InputMethodManager inputMethodManager;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HashMap<String, String> postData = new HashMap<>();
    private PostResponseAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        fragmentManager.beginTransaction().replace(R.id.navigation_header_container, new UserListFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_users) {
            fragmentManager.beginTransaction().replace(R.id.navigation_header_container, new AccountFragment()).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_schedule) {

        } else if (id == R.id.nav_marks) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String login, String password) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData.clear();
        postData.put("login", login);
        postData.put("password", password);
        isLoginExist();
    }

    private void isLoginExist() {
        mTask =
                new PostResponseAsyncTask(this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.contains("yes")) {
                            Snackbar.make(MainActivity.this.getCurrentFocus(),
                                    R.string.error_login_existed,
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            fragmentManager.beginTransaction().
                                    replace(R.id.navigation_header_container, new PersonalInfoFragment())
                                    .commit();
                        }
                    }
                });


        mTask.execute("http://i-gift.tech/is_login_exist.php");
    }

    @Override
    public void onFragmentInteraction(String lastname, String firstname, String patronymic, String gender, Date birth_date) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData.put("lastname", lastname);
        postData.put("firstname", firstname);
        postData.put("patronymic", patronymic);
        postData.put("gender", gender);
        postData.put("birth_date", birth_date.toString());
        fragmentManager.beginTransaction().replace(R.id.navigation_header_container, new ContactsFragment()).commit();
    }

    @Override
    public void onFragmentInteraction(String email, List<String> phones) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        postData.put("email", email);
        //fragmentManager.beginTransaction().remove(mContactsFragment).commit();

        mTask =
                new PostResponseAsyncTask(this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        String msg = "Польователь " +
                                postData.get("lastname") + " " +
                                postData.get("firstname") + " " +
                                postData.get("patronymic") + " ";
                        if (s.contains("true"))
                            msg += "добавлен";
                        else msg += "не добавлен";

                        Snackbar.make(MainActivity.this.getCurrentFocus(), msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });


        mTask.execute("http://i-gift.tech/create_user.php");
    }
}
