package jasic.filip.chatapplication.models;

public class Contact {
    private int fId;
    private String fFirstName;
    private String fLastName;
    private String fUsername;
    public static final String ID = "id";

    public Contact(int Id,String userName,String firstName,String lastName) {
        fId=Id;
        fUsername=userName;
        fFirstName = firstName;
        fLastName=lastName;
    }
    public String getUsername(){return fUsername;}
    public String getFirstName(){ return fFirstName; }
    public String getLastName(){return  fLastName;}
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
    public int getId(){return fId;}

}