package jasic.filip.chatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    Button login,register;
    TextView username,password;
    ContactProvider contactProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactProvider=new ContactProvider(this);

        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        username =  findViewById(R.id.username_text);
        password =  findViewById(R.id.password_text);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_button:
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                if (submitForm()) {
                    Contact contact = contactProvider.getContact(username.getText().toString());

                    if (contact != null) {
                        Intent login_intent = new Intent(MainActivity.this, Main3Activity.class);
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
