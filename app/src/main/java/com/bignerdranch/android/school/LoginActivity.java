package com.bignerdranch.android.school;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.*;
import com.kosalgeek.genasync12.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginView;
    private EditText mPasswordView;
    private Button mSignInViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginView = (EditText) findViewById(R.id.et_login);

        mPasswordView = (EditText) findViewById(R.id.et_password);

        mSignInViewButton = (Button) findViewById(R.id.btn_sign_in);
        mSignInViewButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    private void attemptLogin() {
        mLoginView.setError(null);
        mPasswordView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !Check.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!Check.isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap postData = new HashMap();
            postData.put("login", login);
            postData.put("password", password);

            PostResponseAsyncTask loginTask =
                    new PostResponseAsyncTask(this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("Login Successfully")) {
                                startActivity(new Intent(LoginActivity.this, com.bignerdranch.android.school.MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            loginTask.execute("http://i-gift.tech/login.php");
        }
    }
}

