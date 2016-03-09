package com.example.admin.youlocal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout username_layout;
    TextInputLayout password_layout;
    EditText username_input;
    EditText password_input;
    Button login;
    TextView forgotten_password;
    Animation animationFadeIn;
    Animation animationSlideLeftToRight;
    Animation animationSlideBottomToTop;
    String email;
    String password;

    private static String connection_url = "https://www.youlocalapp.com/oauth2/2.0/signin";
    private static final String Tag_Success = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animationSlideBottomToTop = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_to_top);
        animationSlideLeftToRight = AnimationUtils.loadAnimation(this, R.anim.slide_left_to_right);

        ImageView logo = (ImageView) findViewById(R.id.logo_image);
        logo.startAnimation(animationFadeIn);

        username_layout = (TextInputLayout) findViewById(R.id.username_content);
        password_layout = (TextInputLayout) findViewById(R.id.password_content);
        username_input = (EditText) findViewById(R.id.input_username);
        password_input = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        forgotten_password = (TextView) findViewById(R.id.link_forgotten_password);

        username_layout.startAnimation(animationSlideLeftToRight);
        password_layout.startAnimation(animationSlideLeftToRight);
        login.startAnimation(animationSlideLeftToRight);
        forgotten_password.startAnimation(animationSlideLeftToRight);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectButton(login);
                login();
            }
        });

        forgotten_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                forgotten_pass();
            }
        });

    }

    public void login() {
        if ((username_input.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(),
                    "Username field empty", Toast.LENGTH_SHORT).show();
        } else if ((password_input.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(),
                    "Password field empty", Toast.LENGTH_SHORT).show();
        } else if (!(username_input.getText().toString().matches("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            Toast.makeText(getApplicationContext(),
                    "Email is not the right format", Toast.LENGTH_SHORT).show();
        } else {
            email = username_input.getText().toString();
            password = password_input.getText().toString();

            new Connect().execute();
        }
    }

    public void forgotten_pass() {
        password_layout.setVisibility(View.INVISIBLE);
        login.setAnimation(animationSlideBottomToTop);
        final float positionY = login.getTop();
        login.setY(positionY - 100);
        login.setText("RESET");
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        forgotten_password.setText("BACK TO LOGIN");

        forgotten_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed(positionY);
            }
        });
    }

    public void onBackPressed(float positionY) {
        password_layout.setVisibility(View.VISIBLE);
        login.setText("LOGIN");
        login.setY(positionY);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectButton(login);
                login();
            }
        });
        forgotten_password.setText("FORGOTTEN PASSWORD?");
        forgotten_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                forgotten_pass();
            }
        });
    }

    private void selectButton(Button button) {

        button.setSelected(true);
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= 21) {
            ViewAnimationUtils.createCircularReveal(button,
                    button.getWidth(),
                    button.getHeight(),
                    0,
                    button.getHeight() * 2).start();
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.clickedButton));
        }

    }

    public class Connect extends AsyncTask<String, String, String> {
        String outputMessage = "";

        @Override
        protected String doInBackground(String... params) {
            DataOutputStream printout;
            try {
                URL url = new URL("https://www.youlocalapp.com/oauth2/2.0/signin");

                String param = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&Grant_type=" + URLEncoder.encode("password", "UTF-8");

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setDoOutput(true);
                c.setFixedLengthStreamingMode(param.getBytes().length);
                c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                PrintWriter out = new PrintWriter(c.getOutputStream());
                out.print(param);
                out.close();

                int responseCode = c.getResponseCode(); // getting the response code

                if (responseCode == 200) {
                    StringBuilder responseStrBuilder = new StringBuilder();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    JSONObject response = new JSONObject(responseStrBuilder.toString());

                    String avatar = response.getString("avatar");
                    String fullName = response.getString("fullname");
                    String info = response.getString("aboutMe");

                    User currentUser = new User(avatar, fullName, info);

                    Bundle args = new Bundle();
                    args.putSerializable("avatar", currentUser.getAvatar());
                    args.putSerializable("fullName", currentUser.getFullname());
                    args.putSerializable("aboutMe", currentUser.getInfo());
                    DialogFragment newFragment = new UserInfoFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getSupportFragmentManager(), "user-info");

                    outputMessage = "Successfully Sing In";
                } else {
                    throw new Exception("Username and Password does not match");
                }
            } catch (MalformedURLException e) {
                outputMessage = e.getMessage();
            } catch (IOException e) {
                outputMessage = e.getMessage();
            } catch (JSONException e) {
                outputMessage = e.getMessage();
            } catch (Exception e) {
                outputMessage = e.getMessage();
            }
            return outputMessage;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),outputMessage, Toast.LENGTH_SHORT).show();
        }
    }
}

