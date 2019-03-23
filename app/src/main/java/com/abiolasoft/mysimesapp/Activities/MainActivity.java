package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends BaseActivity {

    //For facebook login
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private GoogleSignInAccount googleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookLoginProp();
        googleSignIn();

    }

    @Override
    protected void onStart(){
        super.onStart();

        if (user != null) {
            updateUI();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 60) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {
            //for facebook login
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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


    private void googleSignIn() {

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleAccount != null) {
            updateUI();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 60);
            }
        });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            googleAccount = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                            UserDetails userDetailsModel = new UserDetails();

                            if (googleAccount != null) {
                                userDetailsModel.setDisplayName(googleAccount.getDisplayName());
                                userDetailsModel.setEmail(googleAccount.getEmail());
                                userDetailsModel.setId(googleAccount.getId());
                                userDetailsModel.setImage_thumb(googleAccount.getPhotoUrl().toString());
                                userDetailsModel.setImage_url(googleAccount.getPhotoUrl().toString());
                            }

                            //must be called under sign in with credentials
                            CurrentUserRepo.updateCurrentUser(userDetailsModel);


                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



    private void updateUI(){
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }


}
