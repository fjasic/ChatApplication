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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        final Button logout=findViewById(R.id.logout_list);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

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

     /*   adapter.addContact(new Contact(("" + getResources().getString(R.string.djordje).toString().charAt(0)),getString(R.string.djordje),getResources().getDrawable(R.drawable.sendbutton),"","",""));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.petar).toString().charAt(0)),getString(R.string.petar),getResources().getDrawable(R.drawable.sendbutton),"","",""));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.ivan).toString().charAt(0)),getString(R.string.ivan),getResources().getDrawable(R.drawable.sendbutton),"","",""));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.jovan).toString().charAt(0)),getString(R.string.jovan),getResources().getDrawable(R.drawable.sendbutton),"","",""));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.milos).toString().charAt(0)),getString(R.string.milos),getResources().getDrawable(R.drawable.sendbutton),"","",""));
*/
        list.setAdapter(adapter);
    }
}
