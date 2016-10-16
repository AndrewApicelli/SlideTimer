package com.example.andrewapicelli.slidetimer;

import android.app.Activity;
import android.app.Application;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by AndrewApicelli on 1/9/16.
 */
public class TimerWidgetListFragment extends ListFragment {

    private TimerWidgetArrayAdapter arrayAdapter;
    ArrayList<SlideTimer> timers = new ArrayList<>();
    SlideTimerDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public void onActivityCreated(Bundle savedInstancestate){
        super.onActivityCreated(savedInstancestate);

        dbHelper = new SlideTimerDBHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();

        getTimers();

        arrayAdapter = new TimerWidgetArrayAdapter(getActivity(), timers);

        setListAdapter(arrayAdapter);

    }

    private void getTimers(){

        timers = new ArrayList<>();

        String[] projection = {
                SlideTimerContract.SlideTimerEntry._ID,
                SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE,
                SlideTimerContract.SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS,
                SlideTimerContract.SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS,
                SlideTimerContract.SlideTimerEntry.COLUMN_NAME_UP_MILLIS,
                SlideTimerContract.SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS,
        };

        String sortOrder = SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                SlideTimerContract.SlideTimerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String title = cursor.getString(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE));
                Long left = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS));
                Long right = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS));
                Long up = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_UP_MILLIS));
                Long down = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS));

                SlideTimer st = new SlideTimer(title,left,right,up,down);
                timers.add(st);

                cursor.moveToNext();
            }
        }

        arrayAdapter = new TimerWidgetArrayAdapter(getActivity(), timers);

        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        SlideTimerCollectionSingleton.getInstance().setCurrentTimer((SlideTimer) l.getItemAtPosition(position));
        launchSwipeActivity(v);


//        Toast.makeText(getActivity().getApplicationContext(),
//                "Click ListItem Number " + position, Toast.LENGTH_LONG)
//                .show();
    }

    private void launchSwipeActivity(View v){
        Intent intent = new Intent(getActivity(), SlideActivity.class);
        startActivity(intent);
    }

    public void updateList(){
        System.out.println("updateList");
        int before = timers.size();
        getTimers();
        int after = timers.size();
        int diff = Math.abs(after - before);

        if(before < after){
            Toast.makeText(getActivity().getApplicationContext(),
                    diff + " items added", Toast.LENGTH_LONG)
                    .show();
        }
        if(before > after){
            Toast.makeText(getActivity().getApplicationContext(),
                    diff + " items removed", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
