package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Adapters.TimetableAdapter;
import com.abiolasoft.mysimesapp.Adapters.TimetableDayAdapter;
import com.abiolasoft.mysimesapp.Models.ImeClass;
import com.abiolasoft.mysimesapp.Models.TimeTablePeriod;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class TimeTableActivity extends BaseActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;

    private List<TimeTablePeriod> timeTableList;
    private TimetableAdapter timetableAdapter;
    private List<ImeClass> imeClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);


        timeTableList = new ArrayList<TimeTablePeriod>();
        imeClass = new ArrayList<ImeClass>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        timetableAdapter = new TimetableAdapter(imeClass, timeTableList);
        recyclerView.setAdapter(timetableAdapter);

        getTimetableFromDb();
    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }

    private void getTimetableFromDb() {

        firebaseFirestore.collection(DbPaths.Classes.toString()).document(CurrentUserRepo.getOffline().getLevel()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    imeClass.add(documentSnapshot.toObject(ImeClass.class));
                    List<TimeTablePeriod> result = documentSnapshot.toObject(ImeClass.class).getTimeTable();
                    String dayExtra = getIntent().getExtras().getString(TimetableDayAdapter.DAY_KEY);

                    timeTableList.clear();

                    if (result.isEmpty()) {
                        Intent addIntent = new Intent(TimeTableActivity.this, UpdatePeriodActivity.class);
                        startActivity(addIntent);
                    }
                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).getDayOfWeek().equals(dayExtra)) {
                            timeTableList.add(result.get(i));
                            timetableAdapter.notifyDataSetChanged();
                        }

                    }
                    Toast.makeText(TimeTableActivity.this, "Table Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
