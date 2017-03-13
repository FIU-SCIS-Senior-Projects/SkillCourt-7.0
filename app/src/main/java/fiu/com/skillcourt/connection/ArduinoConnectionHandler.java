package fiu.com.skillcourt.connection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import fiu.com.skillcourt.interfaces.ArduinoConnectionListener;

/**
 * Created by pedrocarrillo on 10/14/16.
 */

public class ArduinoConnectionHandler implements Runnable {

    private Arduino arduino;
    private boolean connected;
    private Socket socket;
    private ArduinoConnectionListener arduinoConnectionListener;

    public ArduinoConnectionHandler(Arduino arduino, ArduinoConnectionListener arduinoConnectionListener) {
        this.arduino = arduino;
        this.arduinoConnectionListener = arduinoConnectionListener;
    }

    @Override
    public void run() {
        socket = new Socket();
        BufferedReader inStream;
        try {
            socket.connect(new InetSocketAddress(arduino.getIp(), arduino.getPort()));
            connected = true;
            Log.e("Arduino", "connected to "+arduino.getIp());
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            arduinoConnectionListener.onConnect(this);
            while (connected) {
                String hit = inStream.readLine();
                arduinoConnectionListener.onMessageReceived(getCurrent(), hit);
            }

        } catch (IOException e) {
            arduinoConnectionListener.onError("Missed Connection");
            e.printStackTrace();
        }
    }

    public synchronized Arduino.TYPE_LIGHT getCurrent() {
        return arduino.getStatus();
    }

    public void setStatus(Arduino.TYPE_LIGHT status) {
        try {
            arduino.setStatus(status);

            OutputStream outputStream = socket.getOutputStream();
            if (outputStream != null) {
                outputStream.write(status.toString().getBytes());
                outputStream.write('\n');
                outputStream.flush();
            }
        } catch (IOException e) {
            arduinoConnectionListener.onError("Missed Connection while playing");
            e.printStackTrace();
        }

    }

    public void disconnect() {
        if (socket != null && socket.isConnected()) {
            setStatus(Arduino.TYPE_LIGHT.DISCONNECTED);
            connected = false;
            try {
                socket.close();
            } catch (IOException e) {
                arduinoConnectionListener.onError("error at disconnecting");
                e.printStackTrace();
            }
        }
    }

}
