package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.helpers.HttpHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.ContactAdapter;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class ContactActivity extends Activity implements  View.OnClickListener {
  //  private ContactProvider contactProvider;
    private Button logout,refresh;
    private ContactAdapter adapter;
    private String loggedin_username,loggedUserId;
    private TextView loggedInAs;
    private ListView list;

    private HttpHelper httphelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        logout = findViewById(R.id.logout_list);
        refresh=findViewById(R.id.btn_refresh);
        loggedInAs=findViewById(R.id.chatlist);
        list = findViewById(R.id.list);
        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);

        // contactProvider=new ContactProvider(this);

        adapter = new ContactAdapter(this);
        list.setAdapter(adapter);

        // Contact[] contacts=contactProvider.getContacts();

        SharedPreferences sharedPreferences = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        loggedUserId = sharedPreferences.getString(Preferences.USER_LOGGED_IN, null);
        loggedin_username = sharedPreferences.getString("loggedin_username", null);

        loggedInAs.setText(loggedin_username);

        httphelper =new HttpHelper();
        handler=new Handler();
        /*for(Contact contact : contacts){
            if (contact.getId() !=loggedUserId){
                adapter.addContact(contact);
            }
        }*/
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        switch (view.getId()) {
            case R.id.logout_list:

                new Thread(new Runnable() {
                    public void run() {
                        try {

                            final boolean success = httphelper.logOutUserFromServer(ContactActivity.this, LOGOUT_URL);

                            handler.post(new Runnable() {
                                public void run() {
                                    if (!success) {
                                        Toast.makeText(ContactActivity.this, getText(R.string.error_cannot_logout), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intMainactivity = new Intent(ContactActivity.this, MainActivity.class);
                                        startActivity(intMainactivity);
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
                break;

            case R.id.btn_refresh:
                updateContactList();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Updating list
        updateContactList();
    }

        public void updateContactList () {

            new Thread(new Runnable() {
                Contact[] contacts_class;

                public void run() {
                    try {
                        final JSONArray contacts = httphelper.getContactsFromServer(ContactActivity.this, CONTACTS_URL);
                        handler.post(new Runnable() {
                            public void run() {
                                if (contacts != null) {

                                    JSONObject json_contact;
                                    contacts_class = new Contact[contacts.length()];

                                    for (int i = 0; i < contacts.length(); i++) {
                                        try {
                                            json_contact = contacts.getJSONObject(i);
                                            contacts_class[i] = new Contact(json_contact.getString("username"));
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    adapter.update(contacts_class);
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