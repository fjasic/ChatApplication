package jasic.filip.chatapplication.models;

public class Message {
    private int mId;
    private Contact mReceiverId;
    private Contact mSenderId;
    private String mMessage;


    public Message(int Id,Contact senderId,Contact reciverId,String message){
        mMessage=message;
        mId=Id;
        mReceiverId=reciverId;
        mSenderId=senderId;

    }

    public String getMessage(){
        return mMessage;
    }
    public Contact getReceiverId() {return mReceiverId;}
    public Contact getSenderId(){return mSenderId;}
    public int getId(){return mId;}

}
