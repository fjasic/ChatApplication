package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.helpers.HTTPHelper;


public class RegisterActivity extends Activity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText mUsername,mPassword,mFirstname,mLastname,mEmail;
    TextView mDisplayDate;
    Button mRegister_back;
    Handler mHandler;
    HTTPHelper mHTTPHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mUsername =  findViewById(R.id.register_username);
        mPassword = findViewById(R.id.register_password);
        mEmail=findViewById(R.id.johndoe);
        mFirstname=findViewById(R.id.first_name);
        mLastname=findViewById(R.id.last_name);
        mDisplayDate=findViewById(R.id.birth_date);
        mRegister_back=findViewById(R.id.register_page_register_btn);


        Spinner spinner= findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mRegister_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if(submitForm()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put(HTTPHelper.USERNAME, mUsername.getText().toString());
                                jsonObject.put(HTTPHelper.PASSWORD, mPassword.getText().toString());
                                jsonObject.put(HTTPHelper.EMAIL, mEmail.getText().toString());

                                final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_REGISTER, jsonObject);

                                mHandler.post(new Runnable(){
                                    public void run() {
                                        if (res.code == HTTPHelper.CODE_SUCCESS) {
                                            Toast.makeText(RegisterActivity.this, R.string.user_registered, Toast.LENGTH_LONG).show();
                                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(loginIntent);
                                        } else if (res.code == HTTPHelper.CODE_USER_EXISTS) {
                                            Toast.makeText(RegisterActivity.this, R.string.username_taken,
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.error) + " " +
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
            }
        });
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Material_Dialog_MinWidth,mDateSetListener,
                        year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("dateTest","trenutni datum mm/dd/yyyy"+month +"/" + dayOfMonth + "/" + year);
                String date=dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }

    public boolean validateUsername() {
        if (mUsername.getText().toString().trim().isEmpty()) {
            mUsername.setError(getString(R.string.username_error));
            mUsername.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean validatePassword() {
        if (mPassword.getText().toString().trim().length() < 6) {
            mPassword.setError(getString(R.string.password_6_error));
            mPassword.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean validateEmail() {
        if (mEmail.getText().toString().trim().isEmpty()) {
            mEmail.setError(getString(R.string.email_error));
            mEmail.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean submitForm() {
        return validateUsername() && validatePassword() && validateEmail();

    }
}
