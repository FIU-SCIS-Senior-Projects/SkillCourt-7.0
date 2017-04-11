package fiu.com.skillcourt.fcm;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fiu.com.skillcourt.R;

/**
 * Created by alvaro on 4/6/17.
 */

public class SendNotifByHTTP {

    public void sending(String idToken){
        sendDataToServer(idToken);
    }
    //CREATE THE JSON
    private String formatDataAsJSON(String idInstance){
        final JSONObject device = new JSONObject();
        try {
            device.put("token",idInstance);
            return device.toString();
        } catch (JSONException e1){
            e1.printStackTrace();
            Log.d("JWP","Cannot format JSON");
        }
        return null;
    };

    private void sendDataToServer(String token){

        final String json = formatDataAsJSON(token);

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse(json);
            }
        }.execute();


    }

    private String getServerResponse(String json) {
        HttpPost post = new HttpPost("https://limitless-harbor-95760.herokuapp.com/send-notif");

        try {
            StringEntity entity = new StringEntity(json);

            post.setEntity(entity);
            post.setHeader("Content-type","application/json");

            DefaultHttpClient client = new DefaultHttpClient();

            BasicResponseHandler handler = new BasicResponseHandler();
            String response = client.execute(post, handler);

            return response;

        } catch (UnsupportedEncodingException e) {

            Log.d("JWP",e.toString());
        } catch (ClientProtocolException e) {
            Log.d("JWP",e.toString());;
        } catch (IOException e) {
            Log.d("JWP",e.toString());;
        }
        return "UNABLE TO CONNECT SERVER";
    }


}
