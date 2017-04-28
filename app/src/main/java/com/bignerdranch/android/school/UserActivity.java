package com.bignerdranch.android.school;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bignerdranch.android.school.models.Phone;
import com.bignerdranch.android.school.models.User;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private CircleImageView mCircleImageView;
    private TextView mNameTextView, mPhoneTextView, mEmailTextView, mRoleTextView, mInfoTextView;

    HashMap<String, String> postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mCircleImageView = (CircleImageView) findViewById(R.id.iv_photo);
        mNameTextView = (TextView) findViewById(R.id.tv_name);
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
        mEmailTextView = (TextView) findViewById(R.id.tv_email);
        mRoleTextView = (TextView) findViewById(R.id.tv_role);
        mInfoTextView = (TextView) findViewById(R.id.tv_info);

        if (getIntent().getSerializableExtra("user") != null)
            fillIn((User) getIntent().getSerializableExtra("user"));
    }

    private void fillIn(User user) {
        if (!user.photo.isEmpty()) {
            Picasso
                    .with(this)
                    .load(user.photo)
                    .error(R.drawable.ic_menu_camera)
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mCircleImageView);
        }

        mNameTextView.setText(user.lastname + " " + user.firstname + " " + user.patronymic);
        mEmailTextView.setText(user.email);

        postData = new HashMap<>();
        postData.put("user_id", "" + user.id);
        PostResponseAsyncTask taskRole = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String roles) {
                fillInInformation(roles);
            }
        });
        taskRole.execute("http://i-gift.tech/get_user_role.php");

        postData.put("user_id", "" + user.id);
        PostResponseAsyncTask taskPhones = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String phones) {
                fillInPhones(phones);
            }
        });
        taskPhones.execute("http://i-gift.tech/get_user_phones.php");
    }

    private void fillInPhones(String phones) {
        if (!phones.contains("error")) {
            phones = phones.substring(phones.indexOf("["), phones.lastIndexOf("]") + 1);
            ArrayList<Phone> phoneList = new JsonConverter<Phone>().toArrayList(phones, Phone.class);
            phones = "";
            for (Phone phone : phoneList)
                phones += phone.phone + "\n";
        } else phones = "Номера отсутствуют";

        mPhoneTextView.setText(phones);
    }

    private void fillInInformation(String roles) {
        String role = "";

        if (roles.contains("admin")) {
            role = "Администратор ";
        } else if (roles.contains("teacher")) {
            role += "Учитель ";
        } else if (roles.contains("pupil")) {
            role = "Ученик ";
        } else if (roles.contains("parent")) {
            role = "Родитель";
        }
        mRoleTextView.setText(role);

        postData.put("roles", roles);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String info) {
                mInfoTextView.setText(info.replace(".", ".\n"));
            }
        });
        task.execute("http://i-gift.tech/get_info.php");
    }
}
