package com.example.andrewapicelli.slidetimer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by AndrewApicelli on 6/8/16.
 */
public class SettingsController implements View.OnClickListener {

    ToggleButton brightnessToggle;
    ToggleButton volumeToggle;
    ToggleButton rotationToggle;
    SlideActivity slideActivity;

    public SettingsController(SlideActivity slideActivity){
        this.slideActivity = slideActivity;

        brightnessToggle = (ToggleButton) slideActivity.findViewById(R.id.brightnessToggle);
        volumeToggle = (ToggleButton) slideActivity.findViewById(R.id.volumeToggle);
        rotationToggle = (ToggleButton) slideActivity.findViewById(R.id.rotationToggle);

        brightnessToggle.setOnClickListener(this);
        volumeToggle.setOnClickListener(this);
        rotationToggle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.brightnessToggle)
            updateBrightness(brightnessToggle.isEnabled());
        if(v.getId() == R.id.volumeToggle)
            updateVolume(volumeToggle.isEnabled());
        if(v.getId() == R.id.rotationToggle) {
            updateRotation(rotationToggle.isEnabled());
        }

    }

    private void updateBrightness(boolean isEnabled){

    }
    private void updateVolume(boolean isEnabled){

    }
    private void updateRotation(boolean isEnabled){
        int test = isEnabled ? 1 : 0;
        if(ContextCompat.checkSelfPermission(slideActivity,
                Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED){

            Log.i("Check2", "YUP");
            ActivityCompat.requestPermissions(slideActivity, new String[]{Manifest.permission.WRITE_SETTINGS}, 1);
        } else {
            Log.i("Check3","IN");
            Settings.System.putInt(slideActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, test);
        }

    }


}
