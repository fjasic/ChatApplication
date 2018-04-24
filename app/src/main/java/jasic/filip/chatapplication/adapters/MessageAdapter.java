package jasic.filip.chatapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jasic.filip.chatapplication.models.Message;
import jasic.filip.chatapplication.R;


public class MessageAdapter extends BaseAdapter implements View.OnLongClickListener{

    private Context mContext;
    private ArrayList<Message> mMessages;

    public MessageAdapter(Context context) {
        mContext = context;
        mMessages = new ArrayList<>();
    }

    public void addMessage(Message Message) {
        mMessages.add(Message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mMessages.get(position);
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
            view = inflater.inflate(R.layout.message_row, null);
            ViewHolder holder = new ViewHolder();
            holder.message=view.findViewById(R.id.message1);
            view.setTag(holder);
        }

        Message Message = (Message) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.message.setText(Message.mMessage);
        /*if (Message.getBot().equals("user")){
            holder.message.setGravity(Gravity.END);
            holder.message.setBackgroundColor(view.getResources().getColor(R.color.sendBackgroundColor));
            holder.message.setTextColor(view.getResources().getColor(R.color.sendTextColor));
        }else if(Message.getBot().equals("bot")){
            holder.message.setGravity(Gravity.START);
            holder.message.setBackgroundColor(view.getResources().getColor(R.color.recivedBackground));
            holder.message.setTextColor(view.getResources().getColor(R.color.recivedTextColor));
        }*/
        holder.message.setOnLongClickListener(this);
        holder.message.setTag(position);
        return view;
    }

    @Override
    public boolean onLongClick(View view){
        switch (view.getId()) {
            case R.id.message1:
                int i = Integer.parseInt(view.getTag().toString());
                mMessages.remove(i);
                notifyDataSetChanged();
                return true;
            default:
                return false;
        }

    }

    private class ViewHolder {
        public TextView message = null;
    }
}