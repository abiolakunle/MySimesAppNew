package com.abiolasoft.mysimesapp.Application;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

public class ApplicationClass extends Application {
    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
