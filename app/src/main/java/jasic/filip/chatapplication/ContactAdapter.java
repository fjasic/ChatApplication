package jasic.filip.chatapplication;

import android.annotation.SuppressLint;
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


public class ContactAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Contact> mContacts;

    ContactAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<>();
    }

    public void addContact(Contact Contact) {
        mContacts.add(Contact);
        notifyDataSetChanged();
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.contact_row, null);
            ViewHolder holder = new ViewHolder();
            holder.nameFirst=view.findViewById(R.id.nameFirst);
            holder.image = view.findViewById(R.id.nextImage);
            holder.name = view.findViewById(R.id.name);
            view.setTag(holder);
        }

        Contact Contact = (Contact) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nameFirst.setText(Contact.mNameFirst);
        holder.name.setText(Contact.mName);
        holder.image.setImageDrawable(Contact.mImage);
        holder.nameFirst.setBackgroundColor(getRandomColor());

        holder.image.setOnClickListener(this);
        holder.image.setTag(position);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextImage:
                int i = Integer.parseInt(view.getTag().toString());
                Contact clicked = mContacts.get(i);

                if (view.getId() == R.id.nextImage) {
                    Intent intent = new Intent(mContext.getApplicationContext(), Main4Activity.class);
                    intent.putExtra("contact_name", clicked.getName());
                    mContext.startActivity(intent);
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