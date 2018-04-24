package jasic.filip.chatapplication.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jasic.filip.chatapplication.helpers.ChatDBHelper;
import jasic.filip.chatapplication.models.Contact;

public class ContactProvider {

    private ChatDBHelper mHelper=null;

    public ContactProvider(Context context) {
        super();
        mHelper = new ChatDBHelper(context);
    }

    public void insertContact(Contact contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChatDBHelper.COLUMN_USERNAME, contact.getUsername());
        values.put(ChatDBHelper.COLUMN_FIRSTNAME, contact.getFirstName());
        values.put(ChatDBHelper.COLUMN_LASTNAME, contact.getLastName());

        db.insert(ChatDBHelper.CONTACTS_TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(String username) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(ChatDBHelper.CONTACTS_TABLE_NAME, null, ChatDBHelper.COLUMN_USERNAME + "=?",
                new String[] {username}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        db.close();

        return contact;
    }

    public Contact[] getContacts() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(ChatDBHelper.CONTACTS_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        db.close();

        return contacts;
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(ChatDBHelper.CONTACTS_TABLE_NAME, null,
                ChatDBHelper.COLUMN_CONTACT_ID + "=?", new String[] {Integer.toString(id)},
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        db.close();

        return contact;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ChatDBHelper.CONTACTS_TABLE_NAME, ChatDBHelper.COLUMN_CONTACT_ID + "=?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    private Contact createContact(Cursor cursor) {
        int contactId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChatDBHelper.COLUMN_CONTACT_ID)));
        String username = cursor.getString(cursor.getColumnIndex(ChatDBHelper.COLUMN_USERNAME));
        String firstName = cursor.getString(cursor.getColumnIndex(ChatDBHelper.COLUMN_FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndex(ChatDBHelper.COLUMN_LASTNAME));

        return new Contact(contactId, username, firstName, lastName);
    }
}
