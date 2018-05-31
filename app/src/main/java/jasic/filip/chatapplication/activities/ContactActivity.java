package jasic.filip.chatapplication.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jasic.filip.chatapplication.INotificationBinder;
import jasic.filip.chatapplication.INotificationCallback;
import jasic.filip.chatapplication.NotificationService;
import jasic.filip.chatapplication.helpers.HTTPHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.R;
import jasic.filip.chatapplication.adapters.ContactAdapter;
import jasic.filip.chatapplication.utils.Preferences;

public class ContactActivity extends Activity implements View.OnClickListener,ServiceConnection {
    private Button mLogout,mRefresh;
    private ListView mList;
    HTTPHelper mHTTPHelper;
    Handler mHandler;
    String sessionId,loggedUser;
    ContactAdapter mAdapter;

    private INotificationBinder mService=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mLogout=findViewById(R.id.logout_list);
        mList= findViewById(R.id.list);
        mRefresh=findViewById(R.id.refresh);

        mLogout.setOnClickListener(this);
        mRefresh.setOnClickListener(this);

        mAdapter=new ContactAdapter(this);

        SharedPreferences sharedPreferences=getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        sessionId=sharedPreferences.getString(Preferences.SESSION_ID,null);
        loggedUser = sharedPreferences.getString(Preferences.USER_LOGGED_IN, null);

        if (sessionId == null) {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }

        mList.setAdapter(mAdapter);

        bindService(new Intent(ContactActivity.this,NotificationService.class), this,Context.BIND_AUTO_CREATE);

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

                Intent mLogoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                mLogoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mLogoutIntent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(this);
        }
    }

    private void fetchContacts() {
        mAdapter.clear();
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
                                mAdapter.addContact(contact);
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
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        mService = INotificationBinder.Stub.asInterface(iBinder);
        try {
            mService.setCallback(new NotificationCallback());
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
            mService = null;
    }

    private class NotificationCallback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {


            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setContentText("New message!")
                    .setSmallIcon(R.drawable.sendbutton)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.sendbutton))
                    .setContentTitle("Chat application")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final boolean response = mHTTPHelper.getUnreadMessageBool(HTTPHelper.NOTIFICATION_URL,sessionId);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response){
                                    notificationManager.notify(919503, notificationBuilder.build());
                                }else{
                                    Log.d("ERROR","getfromservice");
                                }
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        }
    }

