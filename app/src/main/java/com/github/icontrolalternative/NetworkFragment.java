package com.github.icontrolalternative;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ProcessLifecycleOwner;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
 */
public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";
    private static Future<?> executor;
    private static TelnetReader reader;

    private TelnetCallback<String> callback;
    private TelnetCommandTask telnetCommandTask;


    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, Handler handler) {
        NetworkFragment networkFragment  = (NetworkFragment) fragmentManager.findFragmentByTag(TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            networkFragment.setArguments(args);
            reader = new TelnetReader(handler);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }else{
            reader.setHandler(handler);
        }

         return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        callback = (TelnetCallback<String>) context;
   }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        callback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancel();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    public void sendCommand(String command) {
        telnetCommandTask = new TelnetCommandTask(reader.getTelnet(), callback);
        telnetCommandTask.execute(command);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancel() {
        if (telnetCommandTask != null) {
            telnetCommandTask.cancel(true);
        }
        executor.cancel(true);
    }

    public void startTelnetReader() {
        executor = Executors.newSingleThreadExecutor().submit(reader);
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private static class TelnetCommandTask extends AsyncTask<String, Integer, TelnetCommandTask.Result> {

        private TelnetCallback<String> callback;
        private TelnetClient telnet;

        TelnetCommandTask(TelnetClient telnet, TelnetCallback<String> callback) {
            this.telnet = telnet;
            setCallback(callback);
        }

        void setCallback(TelnetCallback<String> callback) {
            this.callback = callback;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        static class Result {
            public String resultValue;
            public Exception exception;

            public Result(String resultValue) {
                this.resultValue = resultValue;
            }

            public Result(Exception exception) {
                this.exception = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (callback != null) {
                NetworkInfo networkInfo = callback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    callback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected TelnetCommandTask.Result doInBackground(String... commands) {
            Result result = null;
            if (!isCancelled() && commands != null && commands.length > 0) {
                String command = commands[0] + "\r";
                String resultString = "xy";
                try {
                    try {
                        Thread.sleep(100);
                        OutputStream outputStream = telnet.getOutputStream();
                        outputStream.write(command.getBytes());
                        outputStream.flush();

                    } catch (IOException e) {
                        Log.e("Command", "Could not send Command", e);
                    }
                    if (resultString != null) {
                        Log.d("Result", resultString);
                        FlStringConverter conv = new FlStringConverter();
                        result = new Result(conv.hexToString(resultString));
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && callback != null) {
                if (result.exception != null) {
                    callback.updateFromDownload(result.exception.getMessage());
                } else if (result.resultValue != null) {
                    callback.updateFromDownload(result.resultValue);
                }
                callback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }
    }

}

