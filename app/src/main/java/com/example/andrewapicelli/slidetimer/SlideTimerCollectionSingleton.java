package com.example.andrewapicelli.slidetimer;

import android.transition.Slide;

import java.util.ArrayList;

/**
 * Created by AndrewApicelli on 1/9/16.
 */
public class SlideTimerCollectionSingleton {
    private static SlideTimerCollectionSingleton ourInstance = new SlideTimerCollectionSingleton();

    public static SlideTimerCollectionSingleton getInstance() {
        return ourInstance;
    }

    private SlideTimer currentTimer;

    private SlideTimerCollectionSingleton() {}

    public SlideTimer getCurrentTimer(){ return currentTimer; }

    public void setCurrentTimer(SlideTimer newTimer){ currentTimer = newTimer; }

}
