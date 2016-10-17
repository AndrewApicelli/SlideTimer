package com.example.andrewapicelli.slidetimer;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                SlideTimer theTimer = (SlideTimer) parent.getItemAtPosition(position);
                promptDelete(theTimer);
                return true;
            }
        });

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
                int id = cursor.getInt(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_TITLE));
                Long left = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS));
                Long right = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS));
                Long up = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_UP_MILLIS));
                Long down = cursor.getLong(cursor.getColumnIndex(SlideTimerContract.SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS));

                SlideTimer st = new SlideTimer(id, title,left,right,up,down);
                timers.add(st);

                cursor.moveToNext();
            }
        }

        arrayAdapter = new TimerWidgetArrayAdapter(getActivity(), timers);

        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        SlideTimer theTimer = (SlideTimer) l.getItemAtPosition(position);
        launchSwipeActivity(v, theTimer);


//        Toast.makeText(getActivity().getApplicationContext(),
//                "Click ListItem Number " + position, Toast.LENGTH_LONG)
//                .show();
    }



    private void launchSwipeActivity(View v, SlideTimer timer){
        SlideActivity.setIncomingTimer(timer);
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

    private void promptDelete(final SlideTimer timer){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        db.delete(SlideTimerContract.SlideTimerEntry.TABLE_NAME,
                                SlideTimerContract.SlideTimerEntry._ID + "=" + timer.id, null);
                            updateList();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
