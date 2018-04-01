package jasic.filip.chatapplication;
import android.graphics.drawable.Drawable;

public class Contact {
    public String mNameFirst;
    public String mName;
    public Drawable mImage;


    public Contact(String nameFirst,String name, Drawable drawable) {
        mNameFirst=nameFirst;
        mName = name;
        mImage = drawable;
    }
}