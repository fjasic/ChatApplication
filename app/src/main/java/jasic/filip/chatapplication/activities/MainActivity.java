package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.helpers.HttpHelper;
//import jasic.filip.chatapplication.models.Contact;
//import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class MainActivity extends Activity implements  View.OnClickListener{
    Button login,register;
    TextView username,password;
   // ContactProvider contactProvider;

    private Context context;

    private HttpHelper httphelper;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  contactProvider=new ContactProvider(this);

        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        username =  findViewById(R.id.username_text);
        password =  findViewById(R.id.password_text);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        context=this;
        httphelper=new HttpHelper();
        handler=new Handler();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_button:
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                if (submitForm()) {
                    /*Contact contact = contactProvider.getContact(username.getText().toString());

                    if (contact != null) {
                        Intent login_intent = new Intent(MainActivity.this, ContactActivity.class);
                        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(Preferences.USER_LOGGED_IN, contact.getId());
                        editor.apply();

                        startActivity(login_intent);

                    }else {
                        Context context = getApplicationContext();
                        CharSequence text = "Username does not exist";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                */
                    new Thread(new Runnable() {
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("username", username.getText().toString());
                                jsonObject.put("password", password.getText().toString());

                                final boolean response = httphelper.logInUserOnServer(context, LOGIN_URL, jsonObject);

                                handler.post(new Runnable(){
                                    public void run() {
                                        if (response) {
                                            SharedPreferences.Editor editor = context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE).edit();
                                            editor.putString("loggedin_username", username.getText().toString());
                                            editor.apply();

                                            Intent LoginActivity_intent = new Intent(MainActivity.this, ContactActivity.class);
                                            startActivity(LoginActivity_intent);
                                        } else {
                                            SharedPreferences prefs = context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);
                                            String login_error_message="wrong password";
                                            Toast.makeText(MainActivity.this, login_error_message, Toast.LENGTH_SHORT).show();
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
        if (username.getText().toString().trim().isEmpty()) {
            username.setError(getString(R.string.username_error));
            username.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().length() < 6) {
            password.setError(getString(R.string.password_6_error));
            password.requestFocus();
            return false;
        } else {
            return true;
        }

    }

}
