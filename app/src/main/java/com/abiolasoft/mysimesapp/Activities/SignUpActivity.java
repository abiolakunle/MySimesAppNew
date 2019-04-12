package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends AppCompatActivity {

    private TextView displayNameEt, phoneNumberEt, emailEt, passwordEt, confirmPasswordEt;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private FirebaseFirestore firebaseFirestore;
    private SweetAlertDialog pDialog;
    private SweetAlertDialog eDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initMgsDialog();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        displayNameEt = findViewById(R.id.set_display_name_et);
        phoneNumberEt = findViewById(R.id.set_phone_number_et);
        emailEt = findViewById(R.id.set_email_et);
        signUpBtn = findViewById(R.id.sign_up_btn);
        passwordEt = findViewById(R.id.set_password_et);
        confirmPasswordEt = findViewById(R.id.set_confirm_password_et);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromView();
            }
        });


    }

    private void initMgsDialog() {
        eDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        eDialog.getProgressHelper().setBarColor(R.color.primary_dark);
        eDialog.setTitleText("Error");
        eDialog.setCancelable(false);
        eDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.primary_dark);
        pDialog.setCancelable(false);
    }

    private void getDataFromView() {
        displayName = displayNameEt.getText().toString();
        email = emailEt.getText().toString();
        phoneNumber = phoneNumberEt.getText().toString();
        password = passwordEt.getText().toString();
        confirmPassword = confirmPasswordEt.getText().toString();

        if (displayName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || phoneNumber.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            eDialog.setContentText("Please fill all fields appropriately");
            eDialog.show();
        } else {
            if (!password.equals(confirmPassword)) {
                eDialog.setContentText("Passwords do not match");
                eDialog.show();
            } else {
                signUp();
            }
        }


    }

    private void signUp() {
        pDialog.setTitleText("Signing up, Please wait");
        pDialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String userId = task.getResult().getUser().getUid();
                    writeDataToDb(userId);
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    eDialog.setContentText(task.getException().getMessage());
                    eDialog.show();
                }
            }
        });
    }

    private void writeDataToDb(String userId) {
        pDialog.setTitleText("Adding details to Database");
        pDialog.show();
        UserDetails userDetails = new UserDetails();
        userDetails.setDisplayName(displayName);
        userDetails.setEmail(email);
        userDetails.setPhone(phoneNumber);
        userDetails.setId(userId);
        userDetails.setImage_thumb("https://image.flaticon.com/icons/svg/149/149071.svg");
        userDetails.setImage_url("https://image.flaticon.com/icons/svg/149/149071.svg");

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(userDetails);
        CurrentUserRepo.updateCurrentUser(userDetails);
        updateUI();
        pDialog.dismiss();
    }

    private void updateUI() {
        Intent settingsIntent = new Intent(this, AccountSettingsActivity.class);
        settingsIntent.putExtra(SignInActivity.NEW_LOGIN, 0);
        startActivity(settingsIntent);
    }
}
