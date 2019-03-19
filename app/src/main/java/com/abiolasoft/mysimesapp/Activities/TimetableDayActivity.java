package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.abiolasoft.mysimesapp.Adapters.TimetableDayAdapter;
import com.abiolasoft.mysimesapp.R;

public class TimetableDayActivity extends BaseActivity {

    private TimetableDayAdapter timetableDayAdapter;
    private RecyclerView daysRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_day);

        daysRecycler = findViewById(R.id.timetable_day_recycler);

        String[] days = getResources().getStringArray(R.array.Week);

        timetableDayAdapter = new TimetableDayAdapter(days);
        daysRecycler.setAdapter(timetableDayAdapter);
        daysRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }
}
