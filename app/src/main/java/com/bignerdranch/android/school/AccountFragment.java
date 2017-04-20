package com.bignerdranch.android.school;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AccountFragment extends Fragment {

    private OnFragmentListener mListener;

    private ImageView mImageView;
    private EditText mLoginView, mPasswordView, mRepeatPasswordView;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mImageView = (ImageView) view.findViewById(R.id.iv_photo);
        Picasso.with(getContext()).load(R.drawable.owl).transform(new CropCircleTransformation()).into(mImageView);
        mLoginView = (EditText) view.findViewById(R.id.et_login);
        mPasswordView = (EditText) view.findViewById(R.id.et_password);
        mRepeatPasswordView = (EditText) view.findViewById(R.id.et_repeat_password);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_next, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_next) {
            next();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void next() {
        mLoginView.setError(null);
        mPasswordView.setError(null);
        mRepeatPasswordView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repeatPassword = mRepeatPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!repeatPassword.equals(password)) {
            mRepeatPasswordView.setError(getString(R.string.error_passwords_is_different));
            focusView = mRepeatPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password) && !Check.isPasswordValid(password)) {
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
            if (mListener != null) {
                mListener.onFragmentInteraction(login, password);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
