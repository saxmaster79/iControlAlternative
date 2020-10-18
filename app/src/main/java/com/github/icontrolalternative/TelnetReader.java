package com.github.icontrolalternative;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TelnetReader implements Runnable {
    private static final String HOST = "192.168.1.105";
    private static final int FL_TEXT = 1;
    public static final String CONNECTED = "Connected";
    private static final String TAG = "TelnetReader";
    private TelnetClient telnet;
    private Handler handler;
    private String lastString="";

    public TelnetReader(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (telnet == null) {
                    sendMessage("Connecting to "+HOST+"...");
                    telnet = new TelnetClient();
                    telnet.setConnectTimeout(5_000);
                    try {
                        telnet.connect(HOST);
                        sendMessage(CONNECTED);
                    } catch (IOException e) {
                        telnet = null;
                        Log.e(TAG, "Could not connect", e);
                        sleep();
                        continue;
                    }
                }

                InputStream inStream = telnet.getInputStream();
                BufferedReader r = inStream == null ? null : new BufferedReader(new InputStreamReader(inStream));
                if (r != null && r.ready()) {
                    String resultString = new FlStringConverter().hexToString(r.readLine());
                    resultString= handleTicker(resultString);
                    sendMessage(resultString.trim());
                    lastString = resultString;
                } else {
                    sleep();
                }
            }
            Log.i(TAG, "Thread.isInterrupted");
        } catch (IOException ex) {
            Log.e(TAG, "IoException", ex);
        } catch (InterruptedException iue){
            Log.d(TAG, "InterruptedException: ", iue);
        }
    }

    private String handleTicker(String newString) {
        newString = removeAll(newString, "\u0091");//Musical note
        newString = removeAll(newString,"\u0092");//folder icon
        if(lastString.contains(newString)) {
            //Ticker starts showing the Same text
            return lastString;
        }
        //Check if Ticker has moved i chars
        for (int i = 1; i <= 3; i++) {
            if (lastString.endsWith(getAllButLastXChars(newString, i))) {
                return lastString += getLastXChars(newString, i);
            }
        }
        //No Ticker movement recognized
        return newString;
    }

    private String removeAll(String text, String regex){
        return text.replaceAll(regex, "");
    }

    private String getAllButLastXChars(String newString, int x) {
        return newString.substring(0, newString.length() - x);
    }

    private String getLastXChars(String string, int x) {
        return string.substring(string.length() - x);
    }

    private void sendMessage(String string) {
        Message msg = Message.obtain();
        msg.obj = string;
        msg.setTarget(handler);
        msg.sendToTarget();
    }

    private void sleep() throws InterruptedException {

            Thread.sleep(500);

    }

    public TelnetClient getTelnet() {
        return this.telnet;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        sendMessage(CONNECTED);
    }
}
