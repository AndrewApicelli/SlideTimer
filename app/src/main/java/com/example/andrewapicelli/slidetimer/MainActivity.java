package com.example.andrewapicelli.slidetimer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TimerWidgetListFragment timerWidgetListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timerWidgetListFragment = new TimerWidgetListFragment();

        addFragment(timerWidgetListFragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchNewTimerSetupActivity(view);

//                Snackbar.make(view, "Not implemented yet :(", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("Action", "Settings");
            return true;
        }
        if (id == R.id.action_help) {
            Log.i("Action", "Help");
            genHelpDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resumed");
        timerWidgetListFragment.updateList();

    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("stopped");
    }

    @Override
    public void onClick(View v) {
        launchNewTimerSetupActivity(v);
    }

    private void launchNewTimerSetupActivity(View v){
        Intent intent = new Intent(this, NewTimerSetupSlide.class);
        startActivity(intent);
    }

    private void genHelpDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("How To Use");
        String sb = "Welcome to SlideTimer!\n" +
                "\n" +
                "- Click the \"+\" button to add a new slide timer.\n" +
                "- Choose a Title and Left, Right, Up and Down times.\n" +
                "- Click save to store the timer.\n" +
                "- From the main menu select the slide timer you'd like to use.\n" +
                "- Swipe towards the time you'd like to start.\n" +
                "- Double tap for \"Clear Timer\" button.";
        adb.setMessage(sb);
        adb.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog ad = adb.create();
        ad.show();
    }

    private void addFragment(Fragment newFragment){
        getFragmentManager().beginTransaction().add(R.id.main_relative_layout,newFragment, "LIST_FRAG").commit();
    }

}
