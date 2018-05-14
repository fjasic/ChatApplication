package jasic.filip.chatapplication.models;

public class Message {
    private boolean mRecived;
    private String mMessage;


    public Message(String message,boolean recived){
        mMessage=message;
        mRecived=recived;
    }
    public String getMessage(){
        return mMessage;
    }

    public boolean getRecived(){  return mRecived;}

}
