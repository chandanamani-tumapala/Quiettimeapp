package com.example.quiettimeapp.TimeTable;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.quiettimeapp.R;

public class Frag_Fri extends Fragment {
    DataBaseHelper dataBaseHelper;
    WeekScheduleClass FridaySchedule = new WeekScheduleClass();
    SwitchCompat[] switches = new SwitchCompat[9];
    public Frag_Fri() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag__fri, container, false);
        dataBaseHelper = new DataBaseHelper(getContext());

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int column=0;
                switch (buttonView.getId())
                {
                    case R.id.switch1: column=1; break;
                    case R.id.switch2: column=2; break;
                    case R.id.switch3: column=3; break;
                    case R.id.switch4: column=4; break;
                    case R.id.switch5: column=5; break;
                    case R.id.switch6: column=6; break;
                    case R.id.switch7: column=7; break;
                    case R.id.switch8: column=8; break;
                    case R.id.switch9: column=9; break;
                }
                dataBaseHelper.updateWeekSchedule(5,column,isChecked);
            }
        };
        switches[0]=(SwitchCompat) view.findViewById(R.id. switch1 );
        switches[1]=(SwitchCompat) view.findViewById(R.id. switch2 );
        switches[2]=(SwitchCompat) view.findViewById(R.id. switch3 );
        switches[3]=(SwitchCompat) view.findViewById(R.id. switch4 );
        switches[4]=(SwitchCompat) view.findViewById(R.id. switch5 );
        switches[5]=(SwitchCompat) view.findViewById(R.id. switch6 );
        switches[6]=(SwitchCompat) view.findViewById(R.id. switch7 );
        switches[7]=(SwitchCompat) view.findViewById(R.id. switch8 );
        switches[8]=(SwitchCompat) view.findViewById(R.id. switch9 );

        setFragment();

        for(int i=0 ; i<9 ; i++)
            switches[i].setOnCheckedChangeListener(listener);

        return view;
    }
    public void setFragment()
    {
        FridaySchedule = dataBaseHelper.getWeekSchedule(5);
        View view = new View(getContext());

        switches[0].setChecked(FridaySchedule.getFirstPeriod()==1);
        switches[1].setChecked(FridaySchedule.getSecondPeriod()==1);
        switches[2].setChecked(FridaySchedule.getThirdPeriod()==1);
        switches[3].setChecked(FridaySchedule.getFourthPeriod()==1);
        switches[4].setChecked(FridaySchedule.getFifthPeriod()==1);
        switches[5].setChecked(FridaySchedule.getSixthPeriod()==1);
        switches[6].setChecked(FridaySchedule.getSeventhPeriod()==1);
        switches[7].setChecked(FridaySchedule.getEightPeriod()==1);
        switches[8].setChecked(FridaySchedule.getNinethPeriod()==1);
    }
}