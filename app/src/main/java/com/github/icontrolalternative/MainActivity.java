package com.github.icontrolalternative;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;


public class MainActivity extends FragmentActivity implements TelnetCallback<String>, LifecycleObserver {


    private static final String TAG = "MainActivity";
    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                TextView tv = findViewById(R.id.textOut);
                String text = msg.obj.toString();
                if (text == TelnetReader.CONNECTED)
                    sendCommand("?FL");
                tv.setText(text);
            }
        };

        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), handler);
        Button volUp = findViewById(R.id.volUp);
        volUp.setOnTouchListener(new RepeatOnTouchListener(() -> sendCommand("VU")));
        final Button volDown = findViewById(R.id.volDown);
        volDown.setOnTouchListener(new RepeatOnTouchListener(() -> sendCommand("VD")));
    }

    private void sendCommand(String command) {
        if (networkFragment != null) {
            // Execute the async download.
            networkFragment.sendCommand(command);
        }
    }


    public void inputUp(View view) {
        sendCommand("FU");
    }

    public void inputDown(View view) {
        sendCommand("FD");
    }

    @Override
    public void updateFromDownload(String result) {


    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background

        Log.e(TAG, "************* backgrounded");
        Log.e(TAG, "************* ${isActivityVisible()}");
        if (networkFragment != null) {
            networkFragment.cancel();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {

        Log.e(TAG, "************* foregrounded");
        Log.e(TAG, "************* ${isActivityVisible()}");
        // App in foreground
        if (networkFragment != null) {
            networkFragment.startTelnetReader();
        }
    }

}