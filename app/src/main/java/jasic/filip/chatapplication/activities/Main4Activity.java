package jasic.filip.chatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.models.Message;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.MessageAdapter;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.providers.MessageProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {
    private Button send,logout;

    private Contact receiver,sender;
    private ContactProvider contactProvider;

    private EditText msg;
    private MessageAdapter adapterMessage;
    private MessageProvider messageProvider;

    private TextView contactName;
    private ListView listMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        msg=findViewById(R.id.msg);
        contactName = findViewById(R.id.chatName);
        send=findViewById(R.id.send_button);
        logout=findViewById(R.id.logout_message);
        listMessages=findViewById(R.id.listMessage);

        contactProvider=new ContactProvider(this);
        messageProvider=new MessageProvider(this);

        int receiverId = getIntent().getIntExtra(Contact.ID,-1);
        receiver = contactProvider.getContact(receiverId);

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        int senderId = sharedPref.getInt(Preferences.USER_LOGGED_IN, -1);
        sender = contactProvider.getContact(senderId);

        //contactName.setText(receiver.getName());

        adapterMessage=new MessageAdapter(this);
        listMessages.setAdapter(adapterMessage);

        send.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(Main4Activity.this,MainActivity.class);
                startActivity(logoutIntent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        submitForm();
        if (submitForm()) {
            Context context = getApplicationContext();

            CharSequence text = "message is sent";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

           // Message message = new Message(0, sender, receiver, msg.getText().toString());
           // messageProvider.insertMessage(message);

            //adapterMessage.addMessage(message);
            adapterMessage.notifyDataSetChanged();
            msg.setText("");

        }
    }
   /* @Override
    public void onResume() {
        super.onResume();
        fetchMessages();
    }
    private void fetchMessages() {
        Message[] messages = messageProvider.getMessages(sender.getId(), receiver.getId());
        if (messages != null) {
            for (Message message : messages) {
                adapterMessage.addMessage(message);
            }
        }
    }*/

    public boolean validateMsg() {
        if (msg.getText().toString().trim().length() ==0) {
            msg.setError(getString(R.string.empty_message));
            msg.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public boolean submitForm() {
        if (!validateMsg()) {
            return false;
        }
        return true;
    }

}
