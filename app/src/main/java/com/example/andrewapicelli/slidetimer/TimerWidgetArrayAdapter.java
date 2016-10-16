package com.example.andrewapicelli.slidetimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by AndrewApicelli on 1/9/16.
 */
public class TimerWidgetArrayAdapter extends ArrayAdapter<SlideTimer> {
    private final Context context;
    private final ArrayList<SlideTimer> timers;

    public TimerWidgetArrayAdapter(Context context, ArrayList<SlideTimer> timers){
        super(context, R.layout.timer_widget, timers);
        this.context = context;
        this.timers = timers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.timer_widget, parent, false);
        setUpRow(rowView, position);
        return rowView;
    }

    private void setUpRow(View rowView, int position){
        TextView nameView = (TextView) rowView.findViewById(R.id.timer_widget_name);
        nameView.setText(timers.get(position).name);
        TextView leftView = (TextView) rowView.findViewById(R.id.timer_widget_left_time);
        leftView.setText(timers.get(position).leftString);
        TextView rightView = (TextView) rowView.findViewById(R.id.timer_widget_right_time);
        rightView.setText(timers.get(position).rightString);
        TextView upView = (TextView) rowView.findViewById(R.id.timer_widget_up_time);
        upView.setText(timers.get(position).upString);
        TextView downView = (TextView) rowView.findViewById(R.id.timer_widget_down_time);
        downView.setText(timers.get(position).downString);
    }

    //TODO
    //Implement remove, insert and add
//    @Override
//    public void remove(SlideTimer object) {
//        super.remove(object);
//    }
//
//    @Override
//    public void insert(SlideTimer object, int index) {
//        super.insert(object, index);
//    }
//
//    @Override
//    public void add(SlideTimer object) {
//        super.add(object);
//    }

    @Override
    public void addAll(Collection<? extends SlideTimer> newSlideTimers){
        System.out.println("AddAll");

        timers.addAll(newSlideTimers);
        notifyDataSetChanged();
    }
}
