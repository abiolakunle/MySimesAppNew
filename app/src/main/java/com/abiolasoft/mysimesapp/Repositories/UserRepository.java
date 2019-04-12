package com.abiolasoft.mysimesapp.Repositories;

import android.content.Context;

import com.abiolasoft.mysimesapp.Interfaces.UserRepositoryInterface;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.DialogsFactory;
import com.abiolasoft.mysimesapp.Utils.UserSharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UserRepository implements UserRepositoryInterface<UserDetails> {

    private FirebaseFirestore db;
    private Context context;
    private UserSharedPref mPref;

    private List<UserDetails> userList;
    private UserDetails userDetails;
    private DialogsFactory dialogsFactory;


    public UserRepository() {
        this.context = getApplicationContext();
        db = FirebaseFirestore.getInstance();
        dialogsFactory = new DialogsFactory(context);
    }

    @Override
    public List<UserDetails> loadAllUsers() {
        dialogsFactory.pDialog().setContentText("Loading users").show();
        db.collection(DbPaths.Users.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    dialogsFactory.pDialog().dismiss();
                    dialogsFactory.sDialog().show();

                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        userList.add(documentSnapshot.toObject(UserDetails.class));
                    }
                } else {
                    dialogsFactory.pDialog().dismiss();
                    dialogsFactory.eDialog().setContentText(task.getException().getMessage()).show();
                }
            }
        });

        return userList;
    }

    @Override
    public void addUser(final UserDetails userDetails) {
        dialogsFactory.pDialog().setTitleText("Adding to database");
        db.collection(DbPaths.Users.toString()).document(userDetails.getId()).set(userDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialogsFactory.sDialog().show();
                    dialogsFactory.pDialog().dismiss();
                } else {
                    dialogsFactory.pDialog().dismiss();
                    dialogsFactory.eDialog().setContentText(task.getException().getMessage()).show();
                }
            }
        });
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        addUser(userDetails);
    }

    @Override
    public UserDetails getUserById(String user_id) {
        dialogsFactory.pDialog().setTitleText("Loading user");
        db.collection(DbPaths.Users.toString()).document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    dialogsFactory.pDialog().dismiss();
                    dialogsFactory.sDialog().show();
                    userDetails = task.getResult().toObject(UserDetails.class);
                } else {
                    dialogsFactory.pDialog().dismiss();
                    dialogsFactory.eDialog().setContentText(task.getException().getMessage()).show();
                }
            }
        });
        return userDetails;
    }
}