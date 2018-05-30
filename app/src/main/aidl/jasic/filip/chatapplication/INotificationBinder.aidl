// INotificationBinder.aidl
package jasic.filip.chatapplication;

// Declare any non-default types here with import statements
import jasic.filip.chatapplication.INotificationCallback;

interface INotificationBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
 void setCallback(in INotificationCallback callback);
 }
