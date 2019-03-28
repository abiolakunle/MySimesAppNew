package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.abiolasoft.mysimesapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateTimeTableActivity extends AppCompatActivity {

    public static String CLASS_CODE = "classCode";
    private Button addPeriodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_time_table);

        addPeriodBtn = findViewById(R.id.timetable_add_period_btn);

        gotoEditPeriod();

    }

    private void gotoEditPeriod() {
        Intent editIntent = new Intent(this, UpdatePeriodActivity.class);
        editIntent.putExtra(CLASS_CODE, "11");
        startActivity(editIntent);
    }


}
