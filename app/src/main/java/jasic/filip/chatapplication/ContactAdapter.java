package jasic.filip.chatapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public ContactAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<Contact>();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_row, null);
            ViewHolder holder = new ViewHolder();
            holder.nameFirst=view.findViewById(R.id.nameFirst);
            holder.image = (ImageView) view.findViewById(R.id.nextImage);
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        }

        Contact Contact = (Contact) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nameFirst.setText(Contact.mNameFirst);
        holder.name.setText(Contact.mName);
        holder.image.setImageDrawable(Contact.mImage);
        holder.nameFirst.setBackgroundColor(getRandomColor());
        return view;
    }

    private class ViewHolder {
        public TextView nameFirst = null;
        public TextView name = null;
        public ImageView image = null;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}