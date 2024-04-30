package com.example.quiettimeapp.TimeTable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper
{

    public static final String WEEK_SCHEDULE_TABLE = "weekScheduleTable";
    public static final String WEEK_ID = "WeekID";
    public static final String FIRST_PERIOD = "FirstPeriod";
    public static final String SECOND_PERIOD = "SecondPeriod";
    public static final String THIRD_PERIOD = "ThirdPeriod";
    public static final String FOURTH_PERIOD = "FourthPeriod";
    public static final String FIFTH_PERIOD = "FifthPeriod";
    public static final String SIXTH_PERIOD = "SixthPeriod";
    public static final String SEVENTH_PERIOD = "SeventhPeriod";
    public static final String EIGTH_PERIOD = "EigthPeriod";
    public static final String NINETH_PERIOD = "NinethPeriod";

    public DataBaseHelper(@Nullable Context context)
    {
        super(context, "weekSchedule.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createTableStatement = "CREATE TABLE " + WEEK_SCHEDULE_TABLE +" ( " + WEEK_ID + " INTEGER PRIMARY KEY, " + FIRST_PERIOD + " INT, " + SECOND_PERIOD + " INT, " + THIRD_PERIOD + " INT, " + FOURTH_PERIOD + " INT, " + FIFTH_PERIOD + " INT, " + SIXTH_PERIOD + " INT, " + SEVENTH_PERIOD + " INT, " + EIGTH_PERIOD + " INT, " + NINETH_PERIOD + " INT)";
        sqLiteDatabase.execSQL(createTableStatement);
        for(int i=1; i<=5; i++)
        {
            ContentValues cv = new ContentValues();

            cv.put(WEEK_ID, i);
            cv.put(FIRST_PERIOD, 0);
            cv.put(SECOND_PERIOD, 0);
            cv.put(THIRD_PERIOD, 0);
            cv.put(FOURTH_PERIOD, 0);
            cv.put(FIFTH_PERIOD, 0);
            cv.put(SIXTH_PERIOD, 0);
            cv.put(SEVENTH_PERIOD, 0);
            cv.put(EIGTH_PERIOD, 0);
            cv.put(NINETH_PERIOD, 0);

            sqLiteDatabase.insert(WEEK_SCHEDULE_TABLE, null, cv);
        }
//        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void updateWeekSchedule(int row, int column, boolean boolValueToBeSet)
    {
        SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase();
        String PERIOD = null;
        switch(column)
        {
            case 1 : PERIOD = FIRST_PERIOD; break;
            case 2 : PERIOD = SECOND_PERIOD; break;
            case 3 : PERIOD = THIRD_PERIOD; break;
            case 4 : PERIOD = FOURTH_PERIOD; break;
            case 5 : PERIOD = FIFTH_PERIOD; break;
            case 6 : PERIOD = SIXTH_PERIOD; break;
            case 7 : PERIOD = SEVENTH_PERIOD; break;
            case 8 : PERIOD = EIGTH_PERIOD; break;
            case 9 : PERIOD = NINETH_PERIOD; break;
        }
        int intValueToBeSet=0;
        if(boolValueToBeSet) intValueToBeSet=1;
        String queryString = " UPDATE " + WEEK_SCHEDULE_TABLE + " SET " + PERIOD + " = " + intValueToBeSet + " WHERE " + WEEK_ID + " = " + Integer.toString(row) ;
        sqLiteDatabase.execSQL(queryString);
//        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public WeekScheduleClass getWeekSchedule(int id)
    {
        WeekScheduleClass weekScheduleClass = new WeekScheduleClass();
        String queryString = "SELECT * FROM "+ WEEK_SCHEDULE_TABLE + " WHERE " + WEEK_ID + "=" + Integer.toString(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor!=null)
        {
            cursor.moveToFirst();
            weekScheduleClass.setFirstPeriod(cursor.getInt(cursor.getColumnIndex(FIRST_PERIOD)));
            weekScheduleClass.setSecondPeriod(cursor.getInt(cursor.getColumnIndex(SECOND_PERIOD)));
            weekScheduleClass.setThirdPeriod(cursor.getInt(cursor.getColumnIndex(THIRD_PERIOD)));
            weekScheduleClass.setFourthPeriod(cursor.getInt(cursor.getColumnIndex(FOURTH_PERIOD)));
            weekScheduleClass.setFifthPeriod(cursor.getInt(cursor.getColumnIndex(FIFTH_PERIOD)));
            weekScheduleClass.setSixthPeriod(cursor.getInt(cursor.getColumnIndex(SIXTH_PERIOD)));
            weekScheduleClass.setSeventhPeriod(cursor.getInt(cursor.getColumnIndex(SEVENTH_PERIOD)));
            weekScheduleClass.setEightPeriod(cursor.getInt(cursor.getColumnIndex(EIGTH_PERIOD)));
            weekScheduleClass.setNinethPeriod(cursor.getInt(cursor.getColumnIndex(NINETH_PERIOD)));
        }
        else {
           Log.d("getWeekScheduleStatus", "NO DATA IN SQL!");
        }
        cursor.close();
//        db.close();
        return weekScheduleClass;
    }

    public int[] getScheduleAsArray(int id){
        int[] shed=new int[9];
        WeekScheduleClass weekScheduleClass = new WeekScheduleClass();
        String queryString = "SELECT * FROM "+ WEEK_SCHEDULE_TABLE + " WHERE " + WEEK_ID + "=" + Integer.toString(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor!=null )
        {
            cursor.moveToFirst();
            for( int i=0 ; i<9; i++)
            {
                shed[i]=cursor.getInt(i+1);
            }
        }
        else {
            Log.d("getWeekScheduleStatus", "NO DATA IN SQL!");
        }

        cursor.close();
        db.close();
        return shed;
}

}
