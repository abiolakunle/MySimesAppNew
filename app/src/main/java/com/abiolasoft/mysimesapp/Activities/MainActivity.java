package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends BaseActivity {

    //For facebook login
    private CallbackManager callbackManager;
    private LoginButton loginButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facebookLoginProp();
    }

    @Override
    protected void onStart(){
        super.onStart();

        if (user != null) {
            updateUI();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        callbackManager.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected  boolean useToolbar(){
        return false;
    }

    private void facebookLoginProp(){
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult){
                Toast.makeText(MainActivity.this, "Facebook login successful", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel(){
            }

            @Override
            public void onError(FacebookException error){
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser currUser = task.getResult().getUser();

                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    //must be called under sign in with credentials
                    UserDetails userDetailsModel = new UserDetails();
                    for (UserInfo userInfo : currUser.getProviderData()) {
                        //userDetailsModel.setId(userInfo.getUid());
                        userDetailsModel.setDisplayName(userInfo.getDisplayName());
                        userDetailsModel.setEmail(userInfo.getEmail());
                        userDetailsModel.setImage_thumb(userInfo.getPhotoUrl().toString() + "?height=500");
                        userDetailsModel.setImage_url(userInfo.getPhotoUrl().toString()); //?type=large
                        userDetailsModel.setPhone(userInfo.getPhoneNumber());
                    }
                    userDetailsModel.setId(currUser.getUid());
                    CurrentUserRepo.updateCurrentUser(userDetailsModel);

                    updateUI();
                }

            }
        });
    }


    private void updateUI(){
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }


}
