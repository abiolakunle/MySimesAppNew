package com.abiolasoft.mysimesapp.Repositories;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Interfaces.UserRepositoryInterface;
import com.abiolasoft.mysimesapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserRepository implements UserRepositoryInterface<User> {

    private FirebaseFirestore db;
    private Context context;

    private List<User> users;
    private User user;

    public UserRepository(){

    }

    public UserRepository(Context context){
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public List<User> loadAllUsers() {

        db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        users.add(documentSnapshot.toObject(User.class));
                    }
                } else {
                    Toast.makeText(context, "Fail to load users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return users;
    }

    @Override
    public void addUser(User user) {
        db.collection("Users").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateUser(User user) {
        addUser(user);
    }

    @Override
    public User GetUserById(String user_id) {

        db.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return  user;
    }
}
