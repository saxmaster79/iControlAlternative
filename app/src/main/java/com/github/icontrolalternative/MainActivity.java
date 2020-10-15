package com.github.icontrolalternative;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity implements TelnetCallback<String> {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                TextView tv = findViewById(R.id.textOut);
                tv.setText(msg.obj.toString());

            }
        };

        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), handler);

    }
    private void sendCommand(String command) {
        if (networkFragment != null) {
            // Execute the async download.
            networkFragment.sendCommand(command);
        }
    }


    public void volUp(View view) {
        sendCommand("VU");
    }

    public void volDown(View view) {
        sendCommand("VD");
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

        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

}