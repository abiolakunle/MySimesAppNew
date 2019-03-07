package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.DrawerUtil;
import com.abiolasoft.mysimesapp.Utils.UserSharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth.AuthStateListener authStateListener;
    protected FirebaseAuth auth;
    protected FirebaseUser user;
    protected Toolbar toolbar;
    protected final String USER_PREF_KEY = "currentUser";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    user = firebaseAuth.getCurrentUser();

                } else {
                    Intent mainIntent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void setContentView(int layoutResID)
    {
        ConstraintLayout fullView = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (useToolbar())
        {
            setSupportActionBar(toolbar);
            setTitle(this.getClass().getSimpleName());
            UserSharedPref mPref = new UserSharedPref();
            UserDetails currentUserDetails = mPref.getObj(USER_PREF_KEY);
            DrawerUtil drawer = new DrawerUtil();
            drawer.getDrawer(BaseActivity.this, toolbar, currentUserDetails);
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected boolean useToolbar(){
        return true;
    }


}
