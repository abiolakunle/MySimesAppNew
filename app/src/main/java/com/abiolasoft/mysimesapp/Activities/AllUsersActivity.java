package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.abiolasoft.mysimesapp.Adapters.UsersListAdapter;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllUsersActivity extends BaseActivity {

    private List<UserDetails> usersList;
    private RecyclerView usersRecycler;
    private UsersListAdapter usersListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        usersRecycler = findViewById(R.id.users_list_rv);

        usersList = new ArrayList<UserDetails>();

        usersListAdapter = new UsersListAdapter(usersList);
        usersRecycler.setAdapter(usersListAdapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();
    }


    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }

    private void loadUsers() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(DbPaths.Users.toString());

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentSnapshot result : documentSnapshots) {
                    usersList.add(result.toObject(UserDetails.class));
                    usersListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
