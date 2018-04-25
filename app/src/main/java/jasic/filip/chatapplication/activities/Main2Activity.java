package jasic.filip.chatapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.providers.ContactProvider;

public class Main2Activity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    ContactProvider contactProvider;
    EditText username,password,firstname,lastname,email;
    TextView displayDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        username =  findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        email=findViewById(R.id.johndoe);
        firstname=findViewById(R.id.first_name);
        lastname=findViewById(R.id.last_name);
        displayDate=findViewById(R.id.birth_date);

        contactProvider=new ContactProvider(this);

        Spinner spinner= findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button register_back=  findViewById(R.id.register_page_register_btn);
        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if(submitForm()){
                    if(contactProvider.getContact(username.getText().toString())==null){
                        Contact contact=new Contact(0,username.getText().toString(),firstname.getText().toString(),
                                lastname.getText().toString());
                        contactProvider.insertContact(contact);
                        Intent loginIntent=new Intent(getApplicationContext(),MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Username already exist",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Main2Activity.this,android.R.style.Theme_Material_Dialog_MinWidth,dateSetListener,
                        year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("dateTest","trenutni datum mm/dd/yyyy"+month +"/" + dayOfMonth + "/" + year);
                String date=dayOfMonth + "/" + month + "/" + year;
                displayDate.setText(date);
            }
        };

    }


    public boolean validateUsername() {
        if (username.getText().toString().trim().isEmpty()) {
            username.setError(getString(R.string.username_error));
            username.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean validatePassword() {
        if (password.getText().toString().trim().length() < 6) {
            password.setError(getString(R.string.password_6_error));
            password.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean validateEmail() {
        if (email.getText().toString().trim().isEmpty()) {
            email.setError(getString(R.string.email_error));
            email.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean submitForm() {
        return validateUsername() && validatePassword() && validateEmail();

    }
}
