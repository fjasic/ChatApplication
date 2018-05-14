package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.helpers.HTTPHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.ContactAdapter;
import jasic.filip.chatapplication.utils.Preferences;

public class ContactActivity extends Activity implements View.OnClickListener {
    private Button logout,refresh;
    private ListView list;
    HTTPHelper mHTTPHelper;
    Handler mHandler;
    String sessionId,loggedUser;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        logout=findViewById(R.id.logout_list);
        list= findViewById(R.id.list);
        refresh=findViewById(R.id.refresh);

        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);

        adapter=new ContactAdapter(this);

        SharedPreferences sharedPreferences=getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        sessionId=sharedPreferences.getString(Preferences.SESSION_ID,null);
        loggedUser = sharedPreferences.getString(Preferences.USER_LOGGED_IN, null);

        if (sessionId == null) {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }

        list.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_list:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_LOGOUT, new JSONObject(), sessionId);
                            if (res.code == HTTPHelper.CODE_SUCCESS) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), R.string.logged_out,
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), getString(R.string.error) + " " +
                                                res.code + ": " +res.message, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.refresh:
                fetchContacts();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchContacts();
    }

    private void fetchContacts() {
        adapter.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHTTPHelper.getJSONArrayFromURL(HTTPHelper.URL_CONTACTS, sessionId);
                    if (jsonArray == null) {
                        Toast.makeText(ContactActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.getString(HTTPHelper.USERNAME);
                            Contact contact = new Contact(username);
                            if (!username.equals(loggedUser)) {
                                adapter.addContact(contact);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.post(new Runnable(){
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
}
