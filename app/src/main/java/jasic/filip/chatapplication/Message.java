package jasic.filip.chatapplication;

public class Message {
    public String mMessage;
    public String mBot;
    public String mMesssageId;
    public String mReciverId;
    public String mSenderId;

    public Message(String message,String bot,String messageId,String reciverId,String senderId){
        mMessage=message;
        mBot=bot;
        mMesssageId=messageId;
        mReciverId=reciverId;
        mSenderId=senderId;
    }

    public String getMessage(){
        return mMessage;
    }
    public String getReciverId() {return mReciverId;}
    public String getMesssageId(){return mMesssageId;}
    public String getBot() {
        return mBot;
    }
}
