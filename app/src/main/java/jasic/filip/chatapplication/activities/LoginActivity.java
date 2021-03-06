package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.NotificationService;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.helpers.HTTPHelper;
import jasic.filip.chatapplication.utils.Preferences;

public class LoginActivity extends Activity implements  View.OnClickListener{
    Button mLogin,mRegister;
    EditText mUsername,mPassword;
    Handler mHandler;
    HTTPHelper mHTTPHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mHandler = new Handler();
        mHTTPHelper = new HTTPHelper();

        mLogin = findViewById(R.id.login_button);
        mRegister = findViewById(R.id.register_button);
        mUsername =  findViewById(R.id.username_text);
        mPassword =  findViewById(R.id.password_text);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);


    }
    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, NotificationService.class));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_button:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                if (submitForm()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put(HTTPHelper.USERNAME, mUsername.getText().toString());
                                jsonObject.put(HTTPHelper.PASSWORD, mPassword.getText().toString());

                                final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_LOGIN, jsonObject);
                                mHandler.post(new Runnable(){
                                    public void run() {
                                        if (res.code == HTTPHelper.CODE_SUCCESS) {
                                            Toast.makeText(LoginActivity.this, R.string.user_logged_in, Toast.LENGTH_LONG).show();
                                            Intent contactsIntent = new Intent(getApplicationContext(), ContactActivity.class);

                                            SharedPreferences.Editor editor = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE).edit();
                                            editor.putString(Preferences.SESSION_ID, res.sessionId);
                                            editor.putString(Preferences.USER_LOGGED_IN, mUsername.getText().toString());
                                            editor.apply();
                                            startActivity(contactsIntent);
                                        } else if (res.code == HTTPHelper.CODE_INVALID_USER_PASS) {
                                            Toast.makeText(LoginActivity.this, R.string.wrong_user_pass,
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.error) + " " +
                                                    res.code + ": " +res.message, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
        }
    }

    private boolean submitForm() {
        return validateUsername() && validatePassword();
    }

    private boolean validateUsername() {
        if (mUsername.getText().toString().trim().isEmpty()) {
            mUsername.setError(getString(R.string.username_error));
            mUsername.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        if (mPassword.getText().toString().trim().length() < 6) {
            mPassword.setError(getString(R.string.password_6_error));
            mPassword.requestFocus();
            return false;
        } else {
            return true;
        }

    }

}
