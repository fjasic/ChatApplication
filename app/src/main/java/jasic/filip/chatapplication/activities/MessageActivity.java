package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.MessageAdapter;
import jasic.filip.chatapplication.helpers.ChatDBHelper;
import jasic.filip.chatapplication.helpers.HttpHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.models.Message;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.providers.MessageProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class MessageActivity extends Activity implements View.OnClickListener, TextWatcher {

    private Button mButtonLogout, mButtonSend;
    private EditText mMessage;
    private TextView mContactName;
    private ContactProvider mContactProvider;
    private Contact mReceiver, mSender;
    private MessageProvider mMessageProvider;
    private String receiver_username;
    public Message[] messages_fill;
    private ListView messages_list;
    private MessageAdapter mMessageAdapter = new MessageAdapter(this);

    private HttpHelper httphelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    public Context message_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

       // mContactProvider = new ContactProvider(this);
      //  mMessageProvider = new MessageProvider(this);

        mButtonLogout = findViewById(R.id.logout_message);
        mButtonSend = findViewById(R.id.send_button);
        mMessage = findViewById(R.id.msg);
        mContactName = findViewById(R.id.chatName);
        messages_list = findViewById(R.id.listMessage);

        // int receiverId = getIntent().getIntExtra(Contact.ID, -1);
      //  mReceiver = mContactProvider.getContact(receiverId);

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        receiver_username = sharedPref.getString("receiver_username", null);
        receiver_username = sharedPref.getString("receiver_username", null);

        // mSender = mContactProvider.getContact(senderId);

       // mContactName.setText(mReceiver.getName());

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        messages_list.setAdapter(mMessageAdapter);
        message_context = this;

        httphelper = new HttpHelper();

        handler = new Handler();
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        fetchMessages();
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        // Update messages list
        updateMessagesList();
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
             //   Message message = new Message(0, mSender, mReceiver, mMessage.getText().toString());

           //     mMessageProvider.insertMessage(message);

             //   mMessageAdapter.addMessage(message);


             //  mMessageAdapter.notifyDataSetChanged();
             //   mMessage.setText("");
              //  mButtonSend.setEnabled(false);
              //  break;


                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", receiver_username);
                            jsonObject.put("data", mMessage.getText().toString());

                            final boolean success = httphelper.sendMessageToServer(message_context, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (!success) {
                                        Toast.makeText(message_context, getText(R.string.error_message_not_send), Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(message_context, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                        mMessage.getText().clear();
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
                updateMessagesList();
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
/*
    private void fetchMessages() {
        Message[] messages = mMessageProvider.getMessages(mSender.getId(), mReceiver.getId());
        if (messages != null) {
            for (Message message : messages) {
                mMessageAdapter.addMessage(message);
            }
        }
    }*/


    public void updateMessagesList() {

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = httphelper.getMessagesFromServer(message_context, GET_MESSAGE_URL+receiver_username);

                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {

                                JSONObject json_message;
                                messages_fill = new Message[messages.length()];

                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        json_message = messages.getJSONObject(i);
                                        messages_fill[i] = new Message(json_message.getString("sender"),json_message.getString("data"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                mMessageAdapter.update(messages_fill);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();

    }
}
