package com.github.icontrolalternative;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

/**
 * Repeats the same action until the user releases the button
 */
public class RepeatOnTouchListener implements View.OnTouchListener {

    private final Runnable action;
    private Handler mHandler;


    public RepeatOnTouchListener(Runnable action){

        this.action = action;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mHandler != null) return true;
                mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(mAction, 0);
                break;
            case MotionEvent.ACTION_UP:
                if (mHandler == null) return true;
                mHandler.removeCallbacks(mAction);
                mHandler = null;
                break;
        }
        return false;
    }

    Runnable mAction = new Runnable() {
        @Override
        public void run() {
            action.run();
            mHandler.postDelayed(this, 200);
        }
    };

}