package com.example.quiettimeapp.TimeTable;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quiettimeapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class TimetableSetUpActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        }
        catch(Exception e){
            Log.d("exceptions", Arrays.toString(e.getStackTrace()));
        }
        setContentView(R.layout.timeschedule);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewpager2);

        myFragmentStateAdapter adapter = new myFragmentStateAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
        (tab, position) -> {
            tab.setText(getTabTitle(position));
            View Tab = tab.view;
            Tab.setBackgroundColor(Color.parseColor("#4af0a4"));
        }).attach();
    }
    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            default:
                return "Unknown";
        }
    }

    public class myFragmentStateAdapter extends FragmentStateAdapter {
        public myFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new Frag_Mon();
                case 1:
                    return new Frag_Tues();
                case 2:
                    return new Frag_Wed();
                case 3:
                    return new Frag_Thurs();
                case 4:
                    return new Frag_Fri();
                default:
                    return new Frag_Mon();
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}