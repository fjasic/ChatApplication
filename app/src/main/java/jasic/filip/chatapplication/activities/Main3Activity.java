package jasic.filip.chatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.ContactAdapter;
import jasic.filip.chatapplication.providers.ContactProvider;
import jasic.filip.chatapplication.utils.Preferences;

public class Main3Activity extends AppCompatActivity {
    private ContactProvider contactProvider;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        logout=findViewById(R.id.logout_list);

        contactProvider=new ContactProvider(this);

        ContactAdapter adapter=new ContactAdapter(this);
        ListView list= findViewById(R.id.list);
        Contact[] contacts=contactProvider.getContacts();

        SharedPreferences sharedPreferences=getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        int loggedUserId=sharedPreferences.getInt(Preferences.USER_LOGGED_IN,-1);

        for(Contact contact : contacts){
            if (contact.getId() !=loggedUserId){
                adapter.addContact(contact);
            }
        }

        list.setAdapter(adapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
