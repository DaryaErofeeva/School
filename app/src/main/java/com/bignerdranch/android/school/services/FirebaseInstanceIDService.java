package com.bignerdranch.android.school.services;

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Ero on 21.04.2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService implements AsyncResponse {

    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    private void registerToken(String token) {
        HashMap<String, String> postData=new HashMap<>();
        postData.put("token", token);
        PostResponseAsyncTask task=new PostResponseAsyncTask(this, postData, this);
        task.execute("http://i-gift.tech/register.php");
    }

    @Override
    public void processFinish(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
