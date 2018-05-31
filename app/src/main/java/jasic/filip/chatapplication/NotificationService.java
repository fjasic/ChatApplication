package jasic.filip.chatapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {

    NotificationBinder mNotificationBinder;

    @Override
    public IBinder onBind(Intent intent) {
        if (mNotificationBinder == null) {
            mNotificationBinder = new NotificationBinder();
        }

        return mNotificationBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mNotificationBinder.stop();
        return super.onUnbind(intent);
    }

}

