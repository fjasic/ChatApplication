package jasic.filip.chatapplication;
import android.graphics.drawable.Drawable;

public class Contact {
    public String mFirstLetter;
    public String mFirstName;
    public String mLastName;
    public String mUsername;
    public String mContactId;
    public Drawable mImage;


    Contact(String firstLetter, String firstName, Drawable drawable,String lastName,String userName,String contactId) {
        mFirstLetter=firstLetter;
        mFirstName = firstName;
        mLastName=lastName;
        mUsername=userName;
        mContactId=contactId;
        mImage = drawable;

    }

    public String getName(){
        return mFirstName;
    }
    public String getLastName(){return  mLastName;}
    public String getUsername(){return mUsername;}
    public String getContactId(){return mContactId;}
}