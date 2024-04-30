package com.example.quiettimeapp.TimeTable;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Context context;

    int[] onHourList=new  int[]{9,9,10,11,12,13,14,15,16};
    int[] onMinList=new int[]{0,55,55,50,45,45,40,40,35};
    int[] offHourList=new int[]{9,10,11,12,13,14,15,16,17};
    int[] offMinList=new int[]{50,45,45,40,35,35,30,30,25};
    int[] toBeScheduledList = new int[9];

    public void onReceive(Context context, Intent intent) {

        Calendar current=Calendar.getInstance();
        int day = current.get(Calendar.DAY_OF_WEEK)-1;
        if(day >= 5) return;

        this.context = context;
        Log.d("ekkada","1");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        toBeScheduledList = dataBaseHelper.getScheduleAsArray(day);

        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        int currentMin=current.get(Calendar.MINUTE);

        if(currentHour==8 && currentMin==55)
        {
            FindOnSlotAndSchedule(0);
            return;
        }
        else
        {
            for(int index=0;index<9;index++)
            {
                if(currentHour==onHourList[index] && currentMin==onMinList[index])
                {
                    DNDmanager(AudioManager.RINGER_MODE_SILENT);
                    setNextAlarm(offHourList[index], offMinList[index]);
                    return;
                }
                else if(currentHour==offHourList[index] && currentMin==offMinList[index])
                {
                    if( index < 8 && toBeScheduledList[index+1]==1)
                        setNextAlarm(offHourList[index+1], offMinList[index+1]);
                    else
                    {
                        DNDmanager(AudioManager.RINGER_MODE_NORMAL);
                        FindOnSlotAndSchedule(index+1);
                    }
                    return;
                }
            }
        }

        int currentPeriod=0;
        //traverse to find the current period index
        while( currentPeriod<=8 && ConvertToMin(currentHour,currentMin) > ConvertToMin(offHourList[currentPeriod],offMinList[currentPeriod]) )
            currentPeriod++;

        if(currentPeriod>8)
        {
            return;
        }

        if(toBeScheduledList[currentPeriod]==1)
        {
            DNDmanager(AudioManager.RINGER_MODE_SILENT);
            setNextAlarm(offHourList[currentPeriod], offMinList[currentPeriod]);
        }
        else{
            DNDmanager(AudioManager.RINGER_MODE_NORMAL);
            FindOnSlotAndSchedule(currentPeriod+1);
        }
    }

    private int ConvertToMin(int h,int m) { return 60 * h + m; }

    private void FindOnSlotAndSchedule(int fromWhichSlot)
    {
        for (int i = fromWhichSlot; i < 9; i++) {
            if (toBeScheduledList[i] == 1) {
                setNextAlarm(onHourList[i], onMinList[i]);
                break;
            }
        }
    }

    private void setNextAlarm(int hour, int minutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReceiver.class);
        pendingIntent= PendingIntent.getBroadcast(context,69,intent,FLAG_MUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    private void DNDmanager(int whatToDo)
    {
        AudioManager audioManagerobj = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManagerobj.setRingerMode(whatToDo);
    }
}

