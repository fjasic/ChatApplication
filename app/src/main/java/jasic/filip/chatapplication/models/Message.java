package jasic.filip.chatapplication.models;

public class Message {
  //  private int mId;
//    private Contact mReceiverId;
  //  private Contact mSenderId;
    private String mMessage;
    private String mSender;

    public Message(/*int Id,Contact senderId,Contact reciverId,*/String message,String sender){
        mMessage=message;
        mSender=sender;
      //  mId=Id;
       // mReceiverId=reciverId;
       // mSenderId=senderId;

    }

    public String getMessage(){
        return mMessage;
    }
    public String getSender(){
        return mSender;
    }
   // public Contact getReceiverId() {return mReceiverId;}
   // public Contact getSenderId(){return mSenderId;}
    //public int getId(){return mId;}

}
