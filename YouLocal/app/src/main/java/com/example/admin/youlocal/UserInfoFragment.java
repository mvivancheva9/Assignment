package com.example.admin.youlocal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class UserInfoFragment extends DialogFragment {

    public UserInfoFragment() {
        // Empty constructor required for DialogFragment
    }
    URL url;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().getAttributes().windowAnimations = R.style.CustomDialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        String avatar = getArguments().getString("avatar");
        String username = getArguments().getString("fullName");
        String aboutMe = getArguments().getString("aboutMe");

        View view = inflater.inflate(R.layout.dialog_user_info, container);

        try {
            url = new URL(avatar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetBitmap(bitmap, url).execute();

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gravatar_grey);
        }
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap);

        ImageView avatarImage = (ImageView) view.findViewById(R.id.avatar);
        avatarImage.setImageBitmap(circularBitmap);

        TextView usernameTextView = (TextView)view.findViewById(R.id.username_tv);
        usernameTextView.setText(username);

        TextView aboutMeTextView = (TextView)view.findViewById(R.id.aboutMe_tv);
        aboutMeTextView.setText(aboutMe);

        return view;
    }
}
