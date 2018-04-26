package jasic.filip.chatapplication.models;

public class Message {
    private int fId;
    private Contact fReceiverId;
    private Contact fSenderId;
    private String fMessage;


    public Message(int Id,Contact senderId,Contact reciverId,String message){
        fMessage=message;
        fId=Id;
        fReceiverId=reciverId;
        fSenderId=senderId;

    }

    public String getMessage(){
        return fMessage;
    }
    public Contact getReceiverId() {return fReceiverId;}
    public Contact getSenderId(){return fSenderId;}
    public int getId(){return fId;}

}
