package jasic.filip.chatapplication.adapters;


import android.content.Context;
import android.content.Intent;
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
import jasic.filip.chatapplication.activities.Main4Activity;
import jasic.filip.chatapplication.R;


public class ContactAdapter extends BaseAdapter implements View.OnClickListener {

    private Context fContext;
    private ArrayList<Contact> fContacts;

    public ContactAdapter(Context context) {
        fContext = context;
        fContacts = new ArrayList<>();
    }

    public void addContact(Contact Contact) {
        fContacts.add(Contact);
    }

    @Override
    public int getCount() {
        return fContacts.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = fContacts.get(position);
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
            LayoutInflater inflater = (LayoutInflater) fContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.contact_row, null);
            ViewHolder holder = new ViewHolder();
            holder.nameFirst=view.findViewById(R.id.nameFirst);
            holder.image = view.findViewById(R.id.nextImage);
            holder.name = view.findViewById(R.id.name);
            holder.image.setOnClickListener(this);
            holder.image.setTag(position);
            view.setTag(holder);
        }

        Contact Contact = (Contact) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.nameFirst.setText(Contact.getFirstName().substring(0,1).toUpperCase());
        holder.name.setText(Contact.getName());
        holder.nameFirst.setBackgroundColor(getRandomColor());

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextImage:
                int position = Integer.parseInt(view.getTag().toString());
                Contact clicked = fContacts.get(position);

                if (view.getId() == R.id.nextImage) {
                    Intent intent = new Intent(fContext.getApplicationContext(), Main4Activity.class);
                    intent.putExtra(Contact.ID, clicked.getId());
                    fContext.startActivity(intent);
                }
                break;
        }
    }


    private class ViewHolder {
        public TextView nameFirst = null;
        public TextView name = null;
        public ImageView image = null;
    }

    private int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}