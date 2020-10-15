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
    private TelnetClient telnet;
    private final Handler handler;

    public TelnetReader(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {


        try {
            while (true) {
                if (telnet == null) {
                    telnet = new TelnetClient();
                    telnet.setConnectTimeout(5_000);
                    try {
                        telnet.connect(HOST);
                    } catch (IOException e) {
                        telnet = null;
                        Log.e("TelnetReader", "Could not connect", e);
                        sleep();
                        continue;
                    }
                }

                InputStream inStream = telnet.getInputStream();
                BufferedReader r = inStream ==null?null:new BufferedReader(new InputStreamReader(inStream));
                if (r!=null && r.ready()) {
                    String resultString = new FlStringConverter().hexToString(r.readLine());
                    Message msg = Message.obtain();
                    msg.obj = resultString;
                    msg.setTarget(handler);
                    msg.sendToTarget();
                } else {
                    sleep();
                }
            }
        } catch (IOException ex) {
            Log.e("TelnetReader", "IoException", ex);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e("TelnetReader", "sleep", e);
        }
    }

    public TelnetClient getTelnet() {
        return this.telnet;
    }
}
