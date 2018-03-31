package jasic.filip.chatapplication;

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

import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
                    Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                    startActivity(intent);
                }
            }
        });
        final TextView displayDate=findViewById(R.id.birth_date);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Main2Activity.this,android.R.style.Theme_Material_Dialog_MinWidth,dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


    private boolean validateUsername() {
        EditText username =  findViewById(R.id.register_username);
        if (username.getText().toString().trim().isEmpty()) {
            username.setError(getString(R.string.username_error));
            username.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean validatePassword() {
        EditText password = findViewById(R.id.register_password);
        if (password.getText().toString().trim().length() < 6) {
            password.setError(getString(R.string.password_6_error));
            password.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean validateEmail() {
        EditText email=findViewById(R.id.johndoe);
        if (email.getText().toString().trim().isEmpty()) {
            email.setError(getString(R.string.email_error));
            email.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean submitForm(){
        if(!validateUsername()){
            return false;
        }

        if(!validatePassword()){
            return false;
        }

        return validateEmail();
    }
}
