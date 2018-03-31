package jasic.filip.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("4.","otvara 4. activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        final Button send=findViewById(R.id.send_button);
        final Button logout=findViewById(R.id.logout_message);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if(submitForm()){
                    Context context = getApplicationContext();
                    CharSequence text = "message is sent";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    final EditText msg=findViewById(R.id.msg);
                    msg.setText("");}
            }
        });
    }

    private boolean validateMsg() {
        final EditText msg=findViewById(R.id.msg);
        if (msg.getText().toString().trim().length() ==0) {
            msg.setError(getString(R.string.empty_message));
            msg.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean submitForm() {
        if (!validateMsg()) {
            return false;
        }
        return true;
    }

}
