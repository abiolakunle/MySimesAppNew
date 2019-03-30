package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeTableActivity extends BaseActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;

    private List<TimeTablePeriod> timeTableList;
    private TimetableAdapter timetableAdapter;
    private List<ImeClass> imeClass;
    private String dayExtra;

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

        dayExtra = getIntent().getExtras().getString(TimetableDayAdapter.DAY_KEY);

        firebaseFirestore.collection(DbPaths.Classes.toString()).document(CurrentUserRepo.getOffline().getLevel()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    imeClass.clear();
                    imeClass.add(documentSnapshot.toObject(ImeClass.class));
                    imeClass.get(0).setClassCode(CurrentUserRepo.getOffline().getLevel());
                    List<TimeTablePeriod> result = documentSnapshot.toObject(ImeClass.class).getTimeTable();


                    //gets day the item that was deleted by subtracting new from old list
                    /*if(!timeTableList.isEmpty()){
                        timeTableList.removeAll(result);

                        String deletedItemDay = timeTableList.get(0).getDayOfWeek();
                        if(dayExtra != null){
                            dayExtra = deletedItemDay;
                            Toast.makeText(TimeTableActivity.this, "Day of deleted " + deletedItemDay, Toast.LENGTH_SHORT).show();
                        }
                    }*/


                    timeTableList.clear();


                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).getDayOfWeek().equals(dayExtra)) {
                            timeTableList.add(result.get(i));
                            timetableAdapter.notifyDataSetChanged();
                        }
                    }

                    Toast.makeText(TimeTableActivity.this, "Table Loaded", Toast.LENGTH_SHORT).show();
                }

                if (timeTableList.isEmpty()) {
                    Intent addIntent = new Intent(TimeTableActivity.this, UpdatePeriodActivity.class);
                    addIntent.putExtra(TimetableDayAdapter.DAY_KEY, dayExtra);
                    addIntent.putExtra(TimetableAdapter.UPDATE_KEY, 2);
                    addIntent.putExtra(UpdateTimeTableActivity.CLASS_CODE, CurrentUserRepo.getOffline().getLevel());
                    startActivity(addIntent);
                    finish();
                }

            }
        });
    }
}
