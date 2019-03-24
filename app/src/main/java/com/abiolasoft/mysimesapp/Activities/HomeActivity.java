package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.SeedDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends BaseActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SeedDatabase.populateCourses(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        Intent redirectIntent;
        if (auth.getCurrentUser() == null) {
            redirectIntent = new Intent(this, SignInActivity.class);
            startActivity(redirectIntent);
            finish();
        }
        if (CurrentUserRepo.getOffline().getLevel() == null) {
            redirectIntent = new Intent(this, AccountSettingsActivity.class);
            redirectIntent.putExtra(SignInActivity.NEW_LOGIN, 0);
            startActivity(redirectIntent);
            finish();
        }
    }
}
