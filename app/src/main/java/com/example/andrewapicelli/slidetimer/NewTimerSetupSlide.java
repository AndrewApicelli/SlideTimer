package com.example.andrewapicelli.slidetimer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.andrewapicelli.slidetimer.R.id.editTextTitle;
import static com.example.andrewapicelli.slidetimer.R.id.time_right;

public class NewTimerSetupSlide extends AppCompatActivity {

    private ArrayList<EditText> alEditTexts;
    private SlideTimerDBHelper dbHelper;
    private SQLiteDatabase db;
    private TextView textLeft;
    private TextView textRight;
    private TextView textUp;
    private TextView textDown;
    private Timer tLeft = new Timer(textLeft);
    private Timer tRight= new Timer(textRight);
    private Timer tUp = new Timer(textUp);
    private Timer tDown = new Timer(textDown);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SlideTimerDBHelper(getApplicationContext());
        db =  dbHelper.getWritableDatabase();

        textLeft = (TextView) findViewById(R.id.time_left);
        textLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog(tLeft);
            }
        });
        textRight = (TextView) findViewById(R.id.time_right);
        textRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog(tRight);
            }
        });
        textUp = (TextView) findViewById(R.id.time_top);
        textUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog(tUp);
            }
        });
        textDown = (TextView) findViewById(R.id.time_bottom);
        textDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog(tDown);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button saveButton = (Button) findViewById(R.id.newTimerSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    private void launchDialog(Timer timer){


        timer.setMinSec(0, 0);
    }

    private void save(){
        long newTimerMillisLeft = tLeft.getTotalMilli();
        long newTimerMillisRight = tRight.getTotalMilli();
        long newTimerMillisUp = tUp.getTotalMilli();
        long newTimerMillisDown = tDown.getTotalMilli();

        ContentValues values = new ContentValues();
        //values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE, editTextTitle.getText().toString());
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS, newTimerMillisLeft);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS, newTimerMillisRight);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_UP_MILLIS, newTimerMillisUp);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS, newTimerMillisDown);

        long newRowId;
        newRowId = db.insert(SlideTimerContract.SlideTimerEntry.TABLE_NAME, "null", values);

        finish();
    }

}
