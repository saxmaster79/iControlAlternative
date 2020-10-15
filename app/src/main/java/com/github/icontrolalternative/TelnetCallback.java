package com.github.icontrolalternative;

import android.net.NetworkInfo;

public interface TelnetCallback<T> {

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromDownload(T result);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();


    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishDownloading();
}
