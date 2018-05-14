package jasic.filip.chatapplication.models;

public class Contact {

    private String mUsername;
    private String mPassword;
    private String mEmail;

    public static final String ID = "id";

    public Contact(String username,String password,String email) {
        mUsername=username;
        mPassword=password;
        mEmail=email;
    }
    public String getUsername(){return mUsername;}

    public Contact(String username){
        mUsername=username;
    }
}