package jasic.filip.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity {

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
        ListView list= findViewById(R.id.list);
        ContactAdapter adapter=new ContactAdapter(this);
        adapter.addContact(new Contact(("" + getResources().getString(R.string.djordje).toString().charAt(0)),getString(R.string.djordje),getResources().getDrawable(R.drawable.sendbutton)));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.petar).toString().charAt(0)),getString(R.string.petar),getResources().getDrawable(R.drawable.sendbutton)));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.ivan).toString().charAt(0)),getString(R.string.ivan),getResources().getDrawable(R.drawable.sendbutton)));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.jovan).toString().charAt(0)),getString(R.string.jovan),getResources().getDrawable(R.drawable.sendbutton)));
        adapter.addContact(new Contact(("" + getResources().getString(R.string.milos).toString().charAt(0)),getString(R.string.milos),getResources().getDrawable(R.drawable.sendbutton)));
        list.setAdapter(adapter);

    }
}
