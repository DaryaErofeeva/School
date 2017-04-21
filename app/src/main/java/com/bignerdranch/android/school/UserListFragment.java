package com.bignerdranch.android.school;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.StaticImageLoader;
import com.bignerdranch.android.school.models.User;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment implements AsyncResponse {

    View view;
    ListView mListView;
    GridView mGridView;

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_list, container, false);

        PostResponseAsyncTask task = new PostResponseAsyncTask(getContext(), this);
        task.execute("http://i-gift.tech/get_all_users.php");

        return view;
    }

    @Override
    public void processFinish(String result) {
        result = result.substring(result.indexOf("["), result.lastIndexOf("]") + 1);
        ArrayList<User> userList =
                new JsonConverter<User>().toArrayList(result, User.class);

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
                String value = (user.photo.isEmpty() ? "http://i-gift.tech/images/owl.jpg" : user.photo);
                return value;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView view) {
                Picasso.with(getContext())
                        .load(url)
                        .transform(new CropCircleTransformation())
                        .error(R.drawable.owl)
                        .into(view);
            }
        });

        FunDapter<User> adapter = new FunDapter<>(getContext(), userList, R.layout.user_layout, userBindDictionary);

        mListView = (ListView) view.findViewById(R.id.lv_users);
        mListView.setAdapter(adapter);
    }
}
