package com.example.andrewapicelli.slidetimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by AndrewApicelli on 10/17/16.
 */

public class SetTimerDialogFragment extends DialogFragment {


    public interface SetTimerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    SetTimerDialogListener mListener;
    Timer currentTimer;

    public void setTimer(Timer timer){
        currentTimer = timer;
    }

    public int getMin(){
        NumberPicker npMin = (NumberPicker) getView().findViewById(R.id.numberPickerMin);
        return npMin.getValue();
    }
    public int getSec(){
        NumberPicker npSec = (NumberPicker) getView().findViewById(R.id.numberPickerSec);
        return npSec.getValue();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (SetTimerDialogListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement SetTimerDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_adjust_timer, null);

        builder.setView(view)
                .setPositiveButton("save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        NumberPicker npMin = (NumberPicker) view.findViewById(R.id.numberPickerMin);
                        NumberPicker npSec = (NumberPicker) view.findViewById(R.id.numberPickerSec);
                        currentTimer.setMinSec(npMin.getValue(), npSec.getValue());
                        mListener.onDialogPositiveClick(SetTimerDialogFragment.this);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(SetTimerDialogFragment.this);
                    }
                });

        NumberPicker npMin = (NumberPicker) view.findViewById(R.id.numberPickerMin);
        NumberPicker npSec = (NumberPicker) view.findViewById(R.id.numberPickerSec);

        npMin.setMaxValue(59);
        npMin.setMinValue(0);
        npSec.setMaxValue(59);
        npSec.setMinValue(0);
        npMin.setValue(currentTimer.minutes);
        npSec.setValue(currentTimer.seconds);

        return builder.create();

    }

}
