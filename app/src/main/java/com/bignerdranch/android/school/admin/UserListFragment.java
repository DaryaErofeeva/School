package com.bignerdranch.android.school.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.bignerdranch.android.school.AccountFragment;
import com.bignerdranch.android.school.ContactsFragment;
import com.bignerdranch.android.school.OnFragmentListener;
import com.bignerdranch.android.school.PersonalInfoFragment;
import com.bignerdranch.android.school.R;
import com.bignerdranch.android.school.UserActivity;
import com.bignerdranch.android.school.models.User;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener {

    private ArrayList<User> userList;

    private ListView mListView;
    private FloatingActionButton mAddUserButton;

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_list_admin, container, false);

        mListView = (ListView) view.findViewById(R.id.lv_users);

        mAddUserButton = (FloatingActionButton) view.findViewById(R.id.btn_add_user);
        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddUserActivity.class);
                startActivity(intent);
            }
        });

        PostResponseAsyncTask task = new PostResponseAsyncTask(getContext(), this);
        task.execute("http://i-gift.tech/get_all_users.php");

        return view;
    }

    @Override
    public void processFinish(String result) {
        result = result.substring(result.indexOf("["), result.lastIndexOf("]") + 1);
        userList = new JsonConverter<User>().toArrayList(result, User.class);

        BindDictionary<User> userBindDictionary = new BindDictionary<>();

        userBindDictionary.addStringField(R.id.tv_name, new StringExtractor<User>() {
            @Override
            public String getStringValue(User user, int position) {
                return user.lastname + " " + user.firstname + " " + user.patronymic;
            }
        });

        userBindDictionary.addDynamicImageField(R.id.iv_photo, new StringExtractor<User>() {
            @Override
            public String getStringValue(User user, int position) {
                return user.photo;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView view) {
                Picasso
                        .with(getContext())
                        .load(url)
                        .into(view);
            }
        });

        FunDapter<User> adapter = new FunDapter<>(getContext(), userList, R.layout.user_layout, userBindDictionary);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(new Intent(getContext(), UserActivity.class));
        intent.putExtra("user", userList.get(position));
        startActivity(intent);
    }
}
