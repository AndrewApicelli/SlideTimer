package com.example.andrewapicelli.slidetimer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SlideActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
                stopTimer();
                clockText.setText("00:00");
            }
            return false;
        }
    };

    private TextView clockText;
    private GestureDetector swipeDetector;
    private SwipeCountDownTimer timer;
    private Ringtone r;
    private long millisLeft = 0;
    private boolean timerIsPaused = false;
    private MediaPlayer mediaPlayer;
    private Uri defaultRingtoneUri;
    private SlideTimer slideTimer;
    private NotificationCompat.Builder notification;
    private SettingsController settingsController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe);

        mVisible = true;
        mControlsView = findViewById(R.id.OverlayLayout);
        mContentView = findViewById(R.id.fullscreen_content);

        clockText = (TextView) findViewById(R.id.clock_text_large);

        swipeDetector = new GestureDetector(this,this);
        swipeDetector.setOnDoubleTapListener(this);

        defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), defaultRingtoneUri);

        slideTimer = SlideTimerCollectionSingleton.getInstance().getCurrentTimer();
        setSlideLabels();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        Button resetButton = (Button) findViewById(R.id.dummy_button);
        resetButton.setOnTouchListener(mDelayHideTouchListener);

        createNotification();

        settingsController = new SettingsController(this);

//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    private void createNotification(){
        notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.ic_alarm_running_2);
        notification.setContentTitle("Timer is still running...");
        notification.setContentText("Click to jump back to the timer!");
        notification.setAutoCancel(true);

        Intent resultIntent = new Intent(this, SlideActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(SlideActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Pause", "Pausing");
        if (timer != null && !timerIsPaused){
            NotificationManager mNotificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManger.notify(0, notification.build());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("Resume", "Resuming");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("Restart", "Restarting");
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void setSlideLabels(){
        TextView left = (TextView) findViewById(R.id.time_left);
        left.setText(slideTimer.leftString);
        TextView right = (TextView) findViewById(R.id.time_right);
        right.setText(slideTimer.rightString);
        TextView top = (TextView) findViewById(R.id.time_top);
        top.setText(slideTimer.upString);
        TextView bottom = (TextView) findViewById(R.id.time_bottom);
        bottom.setText(slideTimer.downString);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.swipeDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) { }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /*
     *Unlike onSingleTapUp(MotionEvent), this will only be called after the detector
     *is confident that the user's first tap is not followed by a second tap leading
     *to a double-tap gesture.
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if(timer != null) {
            Log.i("Single tap", "Timer not null");
            if (!timerIsPaused) {
                Log.i("Single tap", "Timer not paused");
                timer.cancel();
                timerIsPaused = true;
                mContentView.setBackgroundColor(Color.parseColor("#9E9E9E"));
            }
            else {
                Log.i("Single tap", "Timer paused");
                timer = new SwipeCountDownTimer(millisLeft, 1000);
                timer.start();
                millisLeft = 0;
                timerIsPaused = false;
                mContentView.setBackgroundColor(Color.parseColor("#00E676"));
            }
        }

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event){
        toggle();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;

        try{
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if(Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }

                    result = true;
                }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                if(diffY > 0){
                    onSwipeDown();
                } else {
                    onSwipeUp();
                }

                result = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private void onSwipeRight(){
        //30 seconds
        stopTimer();
//        startTimer(30000);
        startTimer(slideTimer.rightMillis);
    }

    private void onSwipeLeft(){
        //1 min
        stopTimer();
        startTimer(slideTimer.leftMillis);
    }

    private void onSwipeUp(){
        //1 min 30 sec
        stopTimer();
        startTimer(slideTimer.upMillis);
    }

    private void onSwipeDown(){
        //2 min
        stopTimer();
        startTimer(slideTimer.downMillis);
    }


    private void startTimer(long time){
        timer = new SwipeCountDownTimer(time, 100);
        timer.start();
        mContentView.setBackgroundColor(Color.parseColor("#00E676"));
    }

    private void stopTimer(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        mContentView.setBackgroundColor(Color.parseColor("#0099cc"));
    }

    public void tryNotification(){
        Context mContext = getApplicationContext();

        AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                Log.d("Focus Changed", ""+focusChange);
            }
        };

        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener,AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            playNotification();

        am.abandonAudioFocus(focusChangeListener);
    }

    public void playNotification(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SwipeCountDownTimer extends CountDownTimer{

        public SwipeCountDownTimer(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished + 1000;
            millisLeft = millisUntilFinished;
            String ms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            clockText.setText(ms);
        }

        @Override
        public void onFinish() {
            tryNotification();
            mContentView.setBackgroundColor(Color.parseColor("#F44336"));
            clockText.setText("FINISHED!");
            timer = null;
        }
    }
}
