package jasic.filip.chatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.MessageAdapter;
import jasic.filip.chatapplication.helpers.ChatDBHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.models.Message;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.providers.MessageProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button mButtonLogout, mButtonSend;
    private EditText mMessage;
    private MessageAdapter mMessageAdapter;
    private TextView mContactName;
    private ContactProvider mContactProvider;
    private Contact mReceiver, mSender;
    private MessageProvider mMessageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mContactProvider = new ContactProvider(this);
        mMessageProvider = new MessageProvider(this);

        mButtonLogout = findViewById(R.id.logout_message);
        mButtonSend = findViewById(R.id.send_button);
        mMessage = findViewById(R.id.msg);
        mContactName = findViewById(R.id.chatName);

        int receiverId = getIntent().getIntExtra(Contact.ID, -1);
        mReceiver = mContactProvider.getContact(receiverId);

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        int senderId = sharedPref.getInt(Preferences.USER_LOGGED_IN, -1);
        mSender = mContactProvider.getContact(senderId);

        mContactName.setText(mReceiver.getName());

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        mMessageAdapter = new MessageAdapter(this);

        ListView messages = findViewById(R.id.listMessage);

        messages.setAdapter(mMessageAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMessages();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_message:
                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.send_button:
                Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
                Message message = new Message(0, mSender, mReceiver, mMessage.getText().toString());

                mMessageProvider.insertMessage(message);

                mMessageAdapter.addMessage(message);
                mMessageAdapter.notifyDataSetChanged();
                mMessage.setText("");
                mButtonSend.setEnabled(false);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() != 0) {
            mButtonSend.setEnabled(true);
        } else {
            mButtonSend.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void fetchMessages() {
        Message[] messages = mMessageProvider.getMessages(mSender.getId(), mReceiver.getId());
        if (messages != null) {
            for (Message message : messages) {
                mMessageAdapter.addMessage(message);
            }
        }
    }
}
