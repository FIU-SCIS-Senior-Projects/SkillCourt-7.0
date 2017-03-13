package fiu.com.skillcourt.connection;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fiu.com.skillcourt.SkillCourtApp;
import fiu.com.skillcourt.interfaces.ArduinoConnectionListener;
import fiu.com.skillcourt.interfaces.ArduinoSkillCourtInteractor;
import fiu.com.skillcourt.interfaces.Constants;

/**
 * Created by genius on 10/14/16.
 */

public class ArduinoManager implements ArduinoConnectionListener {

    private List<ArduinoConnectionHandler> arduinos;
    private boolean connected = false;
    private Integer countConnected = 0;
    private ArduinoSkillCourtInteractor arduinoSkillCourtInteractor;

    private ArduinoManager arduinoManager;

    private static ArduinoManager ourInstance = new ArduinoManager();

    public static ArduinoManager getInstance() {
        return ourInstance;
    }

    private ArduinoManager() {
        arduinos = new ArrayList<>();
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public int arduinosConnected() {
        return arduinos.size();
    }

    public void startConnection() {

        String host = "192.168.1.232";

        int port = 23;
        Arduino first = new Arduino(host, port, Arduino.TYPE_LIGHT.START);
        arduinos.add(new ArduinoConnectionHandler(first, this));

//        String host2 = "192.168.1.195";
        String host2 = "192.168.1.234";
        int port2 = 23;
        Arduino second = new Arduino(host2, port2, Arduino.TYPE_LIGHT.START);
        arduinos.add(new ArduinoConnectionHandler(second, this));

//        String host2 = "192.168.1.195";
//        String host3 = "192.168.1.235";
//        int port3 = 23;
//        Arduino third = new Arduino(host3, port3, Arduino.TYPE_LIGHT.START);
//        arduinos.add(new ArduinoConnectionHandler(third, this));

//        String host2 = "192.168.1.195";
        String host4 = "192.168.1.236";
        int port4 = 23;
        Arduino fourth = new Arduino(host4, port4, Arduino.TYPE_LIGHT.START);
        arduinos.add(new ArduinoConnectionHandler(fourth, this));


        for (final ArduinoConnectionHandler arduinoConnectionHandler : arduinos) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(arduinoConnectionHandler).start();
                }
            }, 5000);

        }
    }

    @Override
    public void onConnect(ArduinoConnectionHandler arduinoConnectionHandler) {
        if (arduinos.contains(arduinoConnectionHandler)) {
            countConnected++;
            if (countConnected == arduinos.size()) {
                connected = true;
                LocalBroadcastManager.getInstance(SkillCourtApp.getContext()).sendBroadcast(new Intent(Constants.ARDUINOS_CONNECTED));
            }
        }
    }

    @Override
    public void onError(String error) {
        if (connected) {
            countConnected--;
        }
        Intent errorIntent = new Intent(Constants.ARDUINOS_DISCONNECTED);
        errorIntent.putExtra(Constants.errorMessageKey, error);
        LocalBroadcastManager.getInstance(SkillCourtApp.getContext()).sendBroadcast(errorIntent);
        endConnection();
    }

    @Override
    public void onMessageReceived(Arduino.TYPE_LIGHT currentStatus, String message) {
        if (arduinoSkillCourtInteractor != null) {
            arduinoSkillCourtInteractor.onMessageReceived(currentStatus, message);
        }
    }

    public void endConnection() {
        for (final ArduinoConnectionHandler arduinoConnectionHandler : arduinos) {
           arduinoConnectionHandler.disconnect();
        }
        arduinos.clear();
        connected = false;
        arduinoSkillCourtInteractor = null;
    }

    public void setArduinoSkillCourtInteractor(ArduinoSkillCourtInteractor arduinoSkillCourtInteractor) {
        this.arduinoSkillCourtInteractor = arduinoSkillCourtInteractor;
    }

    public List<ArduinoConnectionHandler> getArduinos() {
        return arduinos;
    }

}
