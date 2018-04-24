package jasic.filip.chatapplication.models;

public class Message {
    public String mMessage;
    private int mMesssageId;
    public Contact mReceiverId;
    public Contact mSenderId;

    public Message(int messageId,Contact reciverId,Contact senderId,int id,String message){
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
