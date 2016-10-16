package com.example.andrewapicelli.slidetimer;

import android.provider.BaseColumns;

/**
 * Created by AndrewApicelli on 1/17/16.
 */
public final class SlideTimerContract {

    public SlideTimerContract(){}

    /* Inner class that defines the table contents */
    public static abstract class SlideTimerEntry implements BaseColumns {
        public static final String TABLE_NAME = "slide_timer_table";
        public static final String _ID = "timer_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LEFT_MILLIS = "left_millis";
        public static final String COLUMN_NAME_RIGHT_MILLIS = "right_millis";
        public static final String COLUMN_NAME_UP_MILLIS = "up_millis";
        public static final String COLUMN_NAME_DOWN_MILLIS = "down_millis";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SlideTimerEntry.TABLE_NAME + " (" +
                        SlideTimerEntry._ID + " INTEGER PRIMARY KEY," +
                        SlideTimerEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        SlideTimerEntry.COLUMN_NAME_LEFT_MILLIS + INTEGER_TYPE + COMMA_SEP +
                        SlideTimerEntry.COLUMN_NAME_RIGHT_MILLIS + INTEGER_TYPE + COMMA_SEP +
                        SlideTimerEntry.COLUMN_NAME_UP_MILLIS + INTEGER_TYPE + COMMA_SEP +
                        SlideTimerEntry.COLUMN_NAME_DOWN_MILLIS + INTEGER_TYPE +
                        " )";

        public  static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SlideTimerEntry.TABLE_NAME;
    }

}
