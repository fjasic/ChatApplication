package jasic.filip.chatapplication.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.activities.MessageActivity;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.utils.Preferences;

import static android.content.Context.MODE_PRIVATE;


public class ContactAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public ContactAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<>();
    }
    public void update(Contact[] contacts) {
        mContacts.clear();
        SharedPreferences prefs = mContext.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);

        String loggedin_username = prefs.getString("loggedin_username", null);

        if (contacts != null) {
            for (Contact contact : contacts){
                if (!contact.getUsername().equals(loggedin_username))
                    mContacts.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    public void addContact(Contact Contact) {
        mContacts.add(Contact);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mContacts.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.contact_row, null);
            ViewHolder holder = new ViewHolder();
            holder.nameFirst=view.findViewById(R.id.nameFirst);
            holder.image = view.findViewById(R.id.nextImage);
            holder.name = view.findViewById(R.id.name);
            holder.image.setOnClickListener(this);
            view.setTag(holder);
        }

        Contact Contact = (Contact) getItem(position);
        ViewHolder holder = new ViewHolder();

        //holder.nameFirst.setText(Contact.getUsername().substring(0,1).toUpperCase());
        //holder.name.setText(Contact.getUsername());
        Random rnd = new Random();

      //  holder.nameFirst.setBackgroundColor( Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

        //holder.image.setTag(Contact.getUsername());

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextImage:
                /*int position = Integer.parseInt(view.getTag().toString());
                Contact clicked = mContacts.get(position);

                if (view.getId() == R.id.nextImage) {
                    Intent intent = new Intent(mContext.getApplicationContext(), MessageActivity.class);
                    intent.putExtra(Contact.ID, clicked.getId());
                    mContext.startActivity(intent);
                }*/

                Intent intMessageactivity = new Intent(mContext.getApplicationContext(), MessageActivity.class);

                // Putting receiver userid into SharedPreference file
                SharedPreferences.Editor editor = mContext.getSharedPreferences(Preferences.NAME, MODE_PRIVATE).edit();
                editor.putString("receiver_username", view.getTag().toString());
                editor.apply();

                // Starting message activity
                mContext.startActivity(intMessageactivity);
                break;
        }
    }

    private class ViewHolder {
        public TextView nameFirst = null;
        public TextView name = null;
        public ImageView image = null;
    }

}