package jasic.filip.chatapplication.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jasic.filip.chatapplication.utils.Preferences;

import static android.content.Context.MODE_PRIVATE;

public class HttpHelper {

    private static final int SUCCESS = 200;

    public boolean registerUserOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection ;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        if(responseCode!=SUCCESS){
            SharedPreferences.Editor editor = context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE).edit();
            String err_msg = urlConnection.getResponseMessage();
            editor.putString("register_err_msg",err_msg);
            editor.apply();
        }
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("REGISTER-USER" , urlConnection.getResponseMessage());
        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }
    public boolean logInUserOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection ;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        String sessionid=urlConnection.getHeaderField("sessionid");
        SharedPreferences.Editor editor = context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE).edit();

        if(responseCode==SUCCESS) {
            editor.putString("sessionId",sessionid);
            editor.apply();
        }else{
            String err_msg = urlConnection.getResponseMessage();
            editor.putString("register_err_msg",err_msg);
            editor.apply();
        }

        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("LOGIN-USER" , urlConnection.getResponseMessage());

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public JSONArray getContactsFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();

        urlConnection.disconnect();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("GET-CONTACTS" , urlConnection.getResponseMessage());
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }


    public boolean logOutUserFromServer(Context contacts_context, String urlString) throws IOException,JSONException{

        HttpURLConnection urlConnection ;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = contacts_context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("LOG-OUT" , urlConnection.getResponseMessage());
        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public boolean sendMessageToServer(Context message_context, String urlString, JSONObject jsonObject) throws IOException,JSONException{

        HttpURLConnection urlConnection ;
        java.net.URL url = new URL(urlString);
        SharedPreferences prefs = message_context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
       // urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("SEND-MESSAGE" , urlConnection.getResponseMessage());
        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public JSONArray getMessagesFromServer(Context contacts_context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = contacts_context.getSharedPreferences(Preferences.NAME, MODE_PRIVATE);
        String loggedin_userId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", loggedin_userId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("GET-MESSAGE" , urlConnection.getResponseMessage());
        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

}
