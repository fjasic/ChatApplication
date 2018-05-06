package jasic.filip.chatapplication.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jasic.filip.chatapplication.helpers.ChatDBHelper;
import jasic.filip.chatapplication.models.Contact;
import jasic.filip.chatapplication.models.Message;

public class MessageProvider {
    private ChatDBHelper mHelper;
    private ContactProvider mContactProvider;

    public MessageProvider(Context context){
        super();
        mHelper=new ChatDBHelper(context);
        mContactProvider=new ContactProvider(context);
    }

    public void insertMessage(Message message) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChatDBHelper.COLUMN_SENDER_ID,message.getSenderId().getId());
        values.put(ChatDBHelper.COLUMN_RECEIVER_ID, message.getReceiverId().getId());
        values.put(ChatDBHelper.COLUMN_MESSAGE, message.getMessage());

        db.insert(ChatDBHelper.MESSAGE_TABLE_NAME, null, values);
        db.close();
    }

    public Message[] getMessages() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ChatDBHelper.MESSAGE_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        db.close();

        return messages;
    }

    public Message getMessage(int id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(ChatDBHelper.MESSAGE_TABLE_NAME, null,
                ChatDBHelper.COLUMN_MESSAGE_ID + "=?", new String[] {Integer.toString(id)}, null,
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Message message = createMessage(cursor);

        db.close();

        return message;
    }

    public Message[] getMessages(int contactId1, int contactId2) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(ChatDBHelper.MESSAGE_TABLE_NAME, null,
                "(" + ChatDBHelper.COLUMN_SENDER_ID + "=? AND " + ChatDBHelper.COLUMN_RECEIVER_ID + "=?) OR " +
                        "(" + ChatDBHelper.COLUMN_SENDER_ID + "=? AND " + ChatDBHelper.COLUMN_RECEIVER_ID + "=?)",
                new String[] {Integer.toString(contactId1), Integer.toString(contactId2),
                        Integer.toString(contactId2), Integer.toString(contactId1)},
                null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        db.close();

        return messages;
    }


    public void deleteMessage(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ChatDBHelper.MESSAGE_TABLE_NAME, ChatDBHelper.COLUMN_MESSAGE_ID + "=?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    private Message createMessage(Cursor cursor) {
        int messageId = cursor.getInt(cursor.getColumnIndex(ChatDBHelper.COLUMN_MESSAGE_ID));
        int senderId = cursor.getInt(cursor.getColumnIndex(ChatDBHelper.COLUMN_SENDER_ID));
        int receiverId = cursor.getInt(cursor.getColumnIndex(ChatDBHelper.COLUMN_SENDER_ID));
        String message = cursor.getString(cursor.getColumnIndex(ChatDBHelper.COLUMN_MESSAGE));

        Contact sender = mContactProvider.getContact(senderId);
        Contact receiver = mContactProvider.getContact(receiverId);

        return new Message(messageId,sender,receiver,message);
    }
}
