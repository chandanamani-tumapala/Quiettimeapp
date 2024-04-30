package com.example.quiettimeapp.TimeTable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.quiettimeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag_Mon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_Wed extends Fragment {


    WeekScheduleClass WednesdaySchedule = new WeekScheduleClass();
    SwitchCompat[] switches = new SwitchCompat[9];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frag_Wed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag_Mon.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_Wed newInstance(String param1, String param2) {
        Frag_Wed fragment = new Frag_Wed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag__wed, container, false);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
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
                dataBaseHelper.updateWeekSchedule(3,column,isChecked);
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
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());

        WednesdaySchedule = dataBaseHelper.getWeekSchedule(3);
        View view = new View(getContext());

        switches[0].setChecked(WednesdaySchedule.getFirstPeriod()==1);
        switches[1].setChecked(WednesdaySchedule.getSecondPeriod()==1);
        switches[2].setChecked(WednesdaySchedule.getThirdPeriod()==1);
        switches[3].setChecked(WednesdaySchedule.getFourthPeriod()==1);
        switches[4].setChecked(WednesdaySchedule.getFifthPeriod()==1);
        switches[5].setChecked(WednesdaySchedule.getSixthPeriod()==1);
        switches[6].setChecked(WednesdaySchedule.getSeventhPeriod()==1);
        switches[7].setChecked(WednesdaySchedule.getEightPeriod()==1);
        switches[8].setChecked(WednesdaySchedule.getNinethPeriod()==1);
    }
}