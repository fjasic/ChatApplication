package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import jasic.filip.chatapplication.helpers.HTTPHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.models.Message;
import jasic.filip.chatapplication.utils.Preferences;

public class MessageActivity extends Activity implements View.OnClickListener, TextWatcher {

    private Button mButtonLogout, mButtonSend,mButtonRefresh;
    private EditText mMessage;
    private MessageAdapter mMessageAdapter;
    private TextView mContactName;
    private String mSender;
    ListView messages;
    HTTPHelper mHTTPHelper;
    String mSessionID;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mButtonLogout = findViewById(R.id.logout_message);
        mButtonSend = findViewById(R.id.send_button);
        mButtonRefresh=findViewById(R.id.refresh_msg);
        mMessage = findViewById(R.id.msg);
        mContactName = findViewById(R.id.chatName);
        messages = findViewById(R.id.listMessage);
        mSender = getIntent().getStringExtra(Contact.ID);

        mContactName.setText(mSender);

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        mMessageAdapter = new MessageAdapter(this);

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
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.send_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(HTTPHelper.RECEIVER, mSender);
                            jsonObject.put(HTTPHelper.DATA, mMessage.getText().toString());
                            final HTTPHelper.HTTPResponse response = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_MESSAGE_SEND, jsonObject, mSessionID);

                            if (response.code != HTTPHelper.CODE_SUCCESS) {
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), R.string.unable_to_send_message + "\n" +
                                                response.message, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        mMessageAdapter.addMessage(new Message(mMessage.getText().toString(), false));
                                        mMessageAdapter.notifyDataSetChanged();
                                        mMessage.setText("");
                                        mButtonSend.setEnabled(false);
                                        Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            mHandler.post(new Runnable(){
                                public void run() {
                                    mMessageAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.refresh_msg:
                fetchMessages();
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
        mMessageAdapter.clear();
        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        mSessionID = sharedPref.getString(Preferences.SESSION_ID, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHTTPHelper.getJSONArrayFromURL(HTTPHelper.URL_MESSAGES + mSender, mSessionID);
                    if (jsonArray == null) {
                        Toast.makeText(MessageActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String sender = jsonObject.getString(HTTPHelper.SENDER);
                            String data = jsonObject.getString(HTTPHelper.DATA);
                            Message message = new Message(data, sender.compareTo(mSender) == 0);
                            mMessageAdapter.addMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } /*finally {
                    mHandler.post(new Runnable(){
                        public void run() {
                            mMessageAdapter.notifyDataSetChanged();
                        }
                    });
                }*/
            }
        }).start();
    }
}
