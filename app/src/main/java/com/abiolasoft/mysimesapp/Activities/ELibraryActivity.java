package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.abiolasoft.mysimesapp.Adapters.ELibraryAdapter;
import com.abiolasoft.mysimesapp.Models.EBook;
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

public class ELibraryActivity extends BaseActivity {

    private RecyclerView eLibraryRecycler;
    private ELibraryAdapter eLibraryAdapter;
    private List<EBook> eBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elibrary);

        eBooks = new ArrayList<EBook>();

        eLibraryRecycler = findViewById(R.id.e_library_recycler);
        eLibraryAdapter = new ELibraryAdapter(eBooks);
        eLibraryRecycler.setLayoutManager(new LinearLayoutManager(this));
        eLibraryRecycler.setAdapter(eLibraryAdapter);

        loadBooksFromDb();
    }

    private void loadBooksFromDb() {

        CollectionReference dbRef = FirebaseFirestore.getInstance().collection(DbPaths.ELibrary.toString());

        dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for (DocumentSnapshot result : documentSnapshots) {
                    eBooks.add(result.toObject(EBook.class));
                    eLibraryAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }
}
