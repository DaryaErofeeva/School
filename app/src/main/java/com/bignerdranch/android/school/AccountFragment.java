package com.bignerdranch.android.school;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AccountFragment extends Fragment implements AsyncResponse, View.OnClickListener {

    private OnFragmentListener mListener;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;

    private CircleImageView mImageView;
    private EditText mLoginView, mPasswordView, mRepeatPasswordView;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mImageView = (CircleImageView) view.findViewById(R.id.iv_photo);
        mImageView.setOnClickListener(this);
        mLoginView = (EditText) view.findViewById(R.id.et_login);
        mPasswordView = (EditText) view.findViewById(R.id.et_password);
        mRepeatPasswordView = (EditText) view.findViewById(R.id.et_repeat_password);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
//
        //fileUri = getOutputMediaFileUri();
        ////intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            fileUri = data.getData();
            mImageView.setImageURI(fileUri);
        } else if (resultCode == RESULT_CANCELED) {
            Snackbar
                    .make(
                            getActivity().getCurrentFocus(),
                            R.string.msg_cancelled_image_capture,
                            Snackbar.LENGTH_LONG)
                    .show();
        } else {
            Snackbar
                    .make(
                            getActivity().getCurrentFocus(),
                            R.string.error_image_capture_failed,
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void launchUploadActivity() {
        Intent i = new Intent(getContext(), UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        startActivity(i);
    }

    public Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(AccountFragment.class.getSimpleName(), " Oops! Failed create "
                        + "Android File Upload" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return Uri.fromFile(mediaFile);
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
            callbackIfLoginNotExist();
        }
    }

    private void callbackIfLoginNotExist() {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("login", mLoginView.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(getContext(), postData, this);
        task.execute("http://i-gift.tech/is_login_exist.php");
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
    public void processFinish(String s) {
        if (s.contains("yes")) {
            mLoginView.setError(getString(R.string.error_login_existed));
            mLoginView.requestFocus();
        } else {
            if (mListener != null) {
                mListener.onFragmentInteraction(
                        mLoginView.getText().toString(),
                        mPasswordView.getText().toString()
                );
            }
        }
    }
}
