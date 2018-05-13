package jasic.filip.chatapplication.models;

public class Contact {
   // private int mId;
  //  private String mFirstName;
   // private String mLastName;
    private String mUsername;
   // public static final String ID = "id";

    public Contact(/*int Id,*/String userName/*,String firstName,String lastName*/) {
       // mId=Id;
        mUsername=userName;
        //mFirstName = firstName;
       // mLastName=lastName;
    }
    public String getUsername(){return mUsername;}
    /*public String getFirstName(){ return mFirstName; }
    public String getLastName(){return  mLastName;}
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
    public int getId(){return mId;}
*/
}