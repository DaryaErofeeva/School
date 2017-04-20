package com.bignerdranch.android.school;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentListener mListener;

    private EditText mEmailView;
    private List<EditText> mPhoneViews = new ArrayList<>();
    private FloatingActionButton mAddPhoneFieldButton;
    private LinearLayout mLinearLayout;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_contacts);
        mEmailView = (EditText) view.findViewById(R.id.et_email);
        mPhoneViews.add((EditText) view.findViewById(R.id.et_phone));
        mAddPhoneFieldButton = (FloatingActionButton) view.findViewById(R.id.btn_add_phone);
        mAddPhoneFieldButton.setOnClickListener(this);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {
            submit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        List<String> phones = new ArrayList<>();

        mEmailView.setError(null);
        for (int i = mPhoneViews.size() - 1; i > -1; i--) {
            String phone = mPhoneViews.get(i).getText().toString();

            if (phone.isEmpty()) {
                mPhoneViews.remove(i);
                continue;
            }

            mPhoneViews.get(i).setError(null);
            phones.add(phone);
        }

        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        for (int i = phones.size() - 1; i > -1; i--) {
            if (!Check.isPhoneValid(phones.get(i))) {
                mPhoneViews.get(i).setError(getString(R.string.error_invalid_phone));
                focusView = mPhoneViews.get(i);
                cancel = true;
            }
        }

        if (!Check.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (mListener != null) {
                mListener.onFragmentInteraction(email, phones);
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

    @Override
    public void onClick(View v) {
        EditText editText = new EditText(getContext());
        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(mPhoneViews.get(0).getLayoutParams());
        editText.setHint(R.string.prompt_phone);
        editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editText.setLayoutParams(lp);
        mLinearLayout.addView(editText);
        mPhoneViews.add(editText);
    }
}
