package com.github.icontrolalternative;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity implements DownloadCallback<String> {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager());


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
        // Update your UI here based on result of download.
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        /*switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
            ...
                break;
            case Progress.CONNECT_SUCCESS:
            ...
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
            ...
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
            ...
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
            ...
                break;
        }*/
    }

    @Override
    public void finishDownloading() {

        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

}