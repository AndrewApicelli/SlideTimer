package com.example.andrewapicelli.slidetimer;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

public class NewTimerSetup extends AppCompatActivity implements View.OnClickListener {

    ArrayList<EditText> alEditTexts;
    SlideTimerDBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();
    EditText npLeftMin;
    EditText npLeftSec;
    EditText npRightMin;
    EditText npRightSec;
    EditText npUpMin;
    EditText npUpSec;
    EditText npDownMin;
    EditText npDownSec;
    EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SlideTimerDBHelper(getApplicationContext());
        db =  dbHelper.getWritableDatabase();

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);

        npLeftMin = (EditText) findViewById(R.id.numberPickerLeftMin);
        npLeftSec = (EditText) findViewById(R.id.numberPickerLeftSec);
        npRightMin = (EditText) findViewById(R.id.numberPickerRightMin);
        npRightSec = (EditText) findViewById(R.id.numberPickerRightSec);
        npUpMin = (EditText) findViewById(R.id.numberPickerUpMin);
        npUpSec = (EditText) findViewById(R.id.numberPickerUpSec);
        npDownMin = (EditText) findViewById(R.id.numberPickerDownMin);
        npDownSec = (EditText) findViewById(R.id.numberPickerDownSec);


        alEditTexts = new ArrayList<>();
        alEditTexts.add(npLeftMin);
        alEditTexts.add(npLeftSec);
        alEditTexts.add(npRightMin);
        alEditTexts.add(npRightSec);
        alEditTexts.add(npUpMin);
        alEditTexts.add(npUpSec);
        alEditTexts.add(npDownMin);
        alEditTexts.add(npDownSec);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        Button saveButton = (Button) findViewById(R.id.newTimerSaveButton);
        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        long newTimerMillisLeft = Long.parseLong(npLeftMin.getText().toString())*60*1000 + Long.parseLong(npLeftSec.getText().toString())*1000;
        long newTimerMillisRight = Long.parseLong(npRightMin.getText().toString())*60*1000 + Long.parseLong(npRightSec.getText().toString())*1000;
        long newTimerMillisUp = Long.parseLong(npUpMin.getText().toString())*60*1000 + Long.parseLong(npUpSec.getText().toString())*1000;
        long newTimerMillisDown = Long.parseLong(npDownMin.getText().toString())*60*1000 + Long.parseLong(npDownSec.getText().toString())*1000;

        ContentValues values = new ContentValues();
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE, editTextTitle.getText().toString());
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS, newTimerMillisLeft);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS, newTimerMillisRight);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_UP_MILLIS, newTimerMillisUp);
        values.put(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS, newTimerMillisDown);

        long newRowId;
        newRowId = db.insert(SlideTimerContract.SlideTimerEntry.TABLE_NAME, "null", values);

        finish();
    }
}
