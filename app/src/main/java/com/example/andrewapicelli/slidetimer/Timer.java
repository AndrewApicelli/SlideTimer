package com.example.andrewapicelli.slidetimer;

import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by AndrewApicelli on 10/17/16.
 */

public class Timer {

    TextView tv;
    int minutes = 0;
    int seconds = 0;

    public Timer(TextView tv){
        this.tv = tv;
    }

    public long getMinutesMilli() {
        return minutes*60*1000;
    }

    public long getSecondsMilli() {
        return seconds*1000;
    }

    public long getTotalMilli() {
        return getMinutesMilli() + getSecondsMilli();
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
        updateTextView();
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
        updateTextView();
    }

    public void setMinSec(int minutes, int seconds){
        this.minutes = minutes;
        this.seconds = seconds;
        updateTextView();
    }

    private void updateTextView(){
        String ms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(getMinutesMilli()),
                TimeUnit.MILLISECONDS.toSeconds(getSecondsMilli()));

        tv.setText(ms);
    }
}
