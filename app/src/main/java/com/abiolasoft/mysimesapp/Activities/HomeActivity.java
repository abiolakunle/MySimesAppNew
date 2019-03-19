package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;

import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.SeedDatabase;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SeedDatabase.populateCourses(this);

    }

}
