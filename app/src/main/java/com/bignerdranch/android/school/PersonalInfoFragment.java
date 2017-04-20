package com.bignerdranch.android.school;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

public class PersonalInfoFragment extends Fragment implements View.OnClickListener {

    private TextView mLastnameView, mFirstnameView, mPatronymicView, mBirthdayView;
    private RadioButton mMaleRadioButton;

    private OnFragmentListener mListener;

    Calendar date = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        mLastnameView = (EditText) view.findViewById(R.id.et_lastname);
        mFirstnameView = (EditText) view.findViewById(R.id.et_firstname);
        mPatronymicView = (EditText) view.findViewById(R.id.et_patronymic);
        mBirthdayView = (EditText) view.findViewById(R.id.et_birthday);
        mBirthdayView.setOnClickListener(this);
        mMaleRadioButton = (RadioButton) view.findViewById(R.id.rb_male);

        d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setInitialDate();
            }
        };

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        setDate();
    }

    public void setDate() {
        new DatePickerDialog(getContext(), d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDate() {
        mBirthdayView.setText(DateUtils.formatDateTime(getContext(),
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
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
        mLastnameView.setError(null);
        mFirstnameView.setError(null);
        mPatronymicView.setError(null);
        mBirthdayView.setError(null);

        String lastname = mLastnameView.getText().toString();
        String firstname = mLastnameView.getText().toString();
        String patronymic = mLastnameView.getText().toString();
        Date birthday = Date.valueOf(date.YEAR + "-" + date.MONTH + "-" + date.DAY_OF_MONTH);

        boolean cancel = false;
        View focusView = null;

        if (birthday.getYear() > Calendar.YEAR) {
            mBirthdayView.setError(getString(R.string.error_invalid_date_of_birthday));
            focusView = mBirthdayView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(patronymic) && !Check.isNameValid(patronymic)) {
            mPatronymicView.setError(getString(R.string.error_invalid_patronymic));
            focusView = mPatronymicView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(firstname) && !Check.isNameValid(firstname)) {
            mFirstnameView.setError(getString(R.string.error_invalid_firstname));
            focusView = mFirstnameView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(lastname) && !Check.isNameValid(lastname)) {
            mLastnameView.setError(getString(R.string.error_invalid_lastname));
            focusView = mLastnameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (mListener != null) {
                mListener.onFragmentInteraction(lastname, firstname, patronymic, (mMaleRadioButton.isChecked() ? "m" : "f"), birthday);
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
