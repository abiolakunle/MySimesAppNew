package com.abiolasoft.mysimesapp.Repositories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Interfaces.UserRepositoryInterface;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserRepository implements UserRepositoryInterface<UserDetails> {

    private FirebaseFirestore db;
    private Context context;

    private List<UserDetails> userList;
    private UserDetails userDetails;

    public UserRepository(){

    }

    public UserRepository(Context context){
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public List<UserDetails> loadAllUsers() {

        db.collection("UserDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        userList.add(documentSnapshot.toObject(UserDetails.class));
                    }
                } else {
                    Toast.makeText(context, "Fail to load userDetails", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return userList;
    }

    @Override
    public void addUser(UserDetails userDetails) {
        db.collection("Users").document(userDetails.getId()).set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateUser(UserDetails userDetails) {
        addUser(userDetails);
    }

    @Override
    public UserDetails GetUserById(String user_id) {

        db.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userDetails = documentSnapshot.toObject(UserDetails.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return userDetails;
    }
}
