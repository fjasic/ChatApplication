package jasic.filip.chatapplication;

public class Message {
    public String mMessage;
    public String mBot;

    public Message(String message,String bot){
        mMessage=message;
        mBot=bot;
    }

    public String getMessage(){
        return mMessage;
    }

    public String getBot() {
        return mBot;
    }
}
