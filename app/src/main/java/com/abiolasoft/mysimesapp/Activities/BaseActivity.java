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
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DrawerUtil;
import com.abiolasoft.mysimesapp.Utils.UserSharedPref;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth.AuthStateListener authStateListener;
    protected FirebaseAuth auth;
    protected FirebaseUser user;
    protected GoogleSignInClient mGoogleSignInClient;
    protected Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


/*
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    user = firebaseAuth.getCurrentUser();

                } else {
                    Intent mainIntent = new Intent(BaseActivity.this, SignInActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
*/

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
        Intent menuItemIntent;
        switch (item.getItemId()) {
            case R.id.main_settings_btn:
                menuItemIntent = new Intent(this, AccountSettingsActivity.class);
                startActivity(menuItemIntent);
                break;
            case R.id.main_all_users_btn:
                break;
            case R.id.main_logout_btn:
                sendToLogin();
                break;
        }
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
            UserDetails currentUserDetails = CurrentUserRepo.getOffline();
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


    private void sendToLogin() {

        //function
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(BaseActivity.this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        LoginManager.getInstance().logOut();
                        Intent setupIntent = new Intent(getBaseContext(), SignInActivity.class);
                        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }
}
