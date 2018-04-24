package jasic.filip.chatapplication.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 23.4.2018.
 */

public class ChatDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="chat_app.db";
    public static final int DATABASE_VERSION=1;

    public static final String CONTACTS_TABLE_NAME="contacts";
    public static final String COLUMN_CONTACT_ID="contact_id";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_FIRSTNAME="firstname";
    public static final String COLUMN_LASTNAME="lastname";

    public static final String MESSAGE_TABLE_NAME="message";
    public static final String COLUMN_MESSAGE_ID="message_id";
    public static final String COLUMN_SENDER_ID="sender_id";
    public static final String COLUMN_RECEIVER_ID="receiver_id";
    public static final String COLUMN_MESSAGE="message";

    private SQLiteDatabase mDb=null;
    public ChatDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTACTS_TABLE_NAME + " (" +
            COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME   + " TEXT, " +
            COLUMN_FIRSTNAME  + " TEXT, " +
            COLUMN_LASTNAME   + " TEXT " + ");");

        db.execSQL("CREATE TABLE " + MESSAGE_TABLE_NAME + " (" +
                COLUMN_MESSAGE_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SENDER_ID   + " INTEGER, " +
                COLUMN_RECEIVER_ID + " INTEGER, " +
                COLUMN_MESSAGE     + " TEXT, " +
                " FOREIGN KEY (" + COLUMN_SENDER_ID + ") REFERENCES " + ChatDBHelper.CONTACTS_TABLE_NAME + "(" + ChatDBHelper.COLUMN_CONTACT_ID + ")," +
                " FOREIGN KEY (" + COLUMN_RECEIVER_ID + ") REFERENCES " + ChatDBHelper.CONTACTS_TABLE_NAME + "(" + ChatDBHelper.COLUMN_CONTACT_ID + ")" +
                ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
