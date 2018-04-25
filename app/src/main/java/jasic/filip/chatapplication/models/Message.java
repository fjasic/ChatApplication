package jasic.filip.chatapplication.models;

public class Message {
    private int mMesssageId;
    private Contact mReceiverId;
    private Contact mSenderId;
    private String mMessage;


    public Message(int messageId,Contact senderId,Contact reciverId,String message){
        mMessage=message;
        mMesssageId=messageId;
        mReceiverId=reciverId;
        mSenderId=senderId;

    }

    public String getMessage(){
        return mMessage;
    }
    public Contact getReceiverId() {return mReceiverId;}
    public Contact getSenderId(){return mSenderId;}
    public int getMesssageId(){return mMesssageId;}

}
