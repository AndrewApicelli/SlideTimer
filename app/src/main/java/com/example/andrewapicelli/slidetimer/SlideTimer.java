package com.example.andrewapicelli.slidetimer;

import java.util.concurrent.TimeUnit;

/**
 * Created by AndrewApicelli on 1/9/16.
 */
public class SlideTimer {

    String name;

    long leftMillis;
    long rightMillis;
    long upMillis;
    long downMillis;

    String leftString;
    String rightString;
    String upString;
    String downString;

    public SlideTimer(String name, long leftMillis, long rightMillis, long upMillis, long downMillis){
        this.name = name;
        this.leftMillis = leftMillis;
        this.rightMillis = rightMillis;
        this.upMillis = upMillis;
        this.downMillis = downMillis;
        setLabels();
    }

    private void setLabels(){
        leftString = makeLabel(leftMillis);
        rightString = makeLabel(rightMillis);
        upString = makeLabel(upMillis);
        downString = makeLabel(downMillis);
    }

    private String makeLabel(long millis){
        String label = String.format("%02d:%02d",
//                    TimeUnit.MILLISECONDS.toHours(millisLeft),
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        return label;
    }
}
