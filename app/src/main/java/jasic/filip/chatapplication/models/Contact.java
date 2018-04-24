package jasic.filip.chatapplication.models;

public class Contact {
    public int mId;
    public String mFirstName;
    public String mLastName;
    public String mUsername;


    public Contact(int Id,String userName,String firstName,String lastName) {
        mId=Id;
        mUsername=userName;
        mFirstName = firstName;
        mLastName=lastName;
    }
    public int getId(){return mId;}
    public String getUsername(){return mUsername;}
    public String getFirstName(){ return mFirstName; }
    public String getLastName(){return  mLastName;}
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

}