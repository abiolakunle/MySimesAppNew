package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Models.Course;
import com.abiolasoft.mysimesapp.Models.ImeClass;
import com.abiolasoft.mysimesapp.Models.TimeTablePeriod;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdatePeriodActivity extends BaseActivity {

    private FirebaseFirestore firebaseFirestore;
    private Spinner courseSpinner, startTimeHourSpin, endTimeHourSpin, startTimeMinSpin, endTimeMinSpin, dayOfWeekSpin;
    private Spinner startAmPm, endAmPm;
    private CheckBox isPracticalCheck;
    private Button updateBtn;
    private TextView lecturerTxt;
    private List<Course> courseObjects;
    private boolean dataValid = true;
    private ImeClass imeClass;
    private TimeTablePeriod timeTablePeriod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_period);

        firebaseFirestore = FirebaseFirestore.getInstance();

        courseSpinner = findViewById(R.id.timetable_course_spinner);
        dayOfWeekSpin = findViewById(R.id.day_of_week_spin);
        updateBtn = findViewById(R.id.update_timetable_btn);
        startTimeHourSpin = findViewById(R.id.start_hour);
        startTimeMinSpin = findViewById(R.id.start_min);
        endTimeHourSpin = findViewById(R.id.end_hour);
        endTimeMinSpin = findViewById(R.id.end_min);
        lecturerTxt = findViewById(R.id.lecturer_name);
        isPracticalCheck = findViewById(R.id.is_practical_check);
        startAmPm = findViewById(R.id.start_am_pm);
        endAmPm = findViewById(R.id.end_am_pm);


        populateSpinners();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDataFromViews();
                if (dataValid) {
                    submitToDb();
                }
            }
        });
    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }

    private void submitToDb() {

        firebaseFirestore.collection(DbPaths.Classes.toString()).document("11").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ImeClass result = imeClass;
                    if (task.getResult().exists()) {
                        result = task.getResult().toObject(ImeClass.class);
                    }

                    result.addToTimetable(timeTablePeriod);
                    firebaseFirestore.collection(DbPaths.Classes.toString()).document("11").set(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdatePeriodActivity.this, "Class timetable Updated", Toast.LENGTH_SHORT).show();
                                Intent timetableIntent = new Intent(UpdatePeriodActivity.this, TimeTableActivity.class);
                                startActivity(timetableIntent);
                            }
                        }
                    });
                }
            }
        });


    }

    private void getDataFromViews() {
        imeClass = new ImeClass();

        // String classCode = getIntent().getStringExtra("classCode");
        imeClass.setClassCode(11);

        timeTablePeriod = new TimeTablePeriod();
        int selectedCoursePosition = courseSpinner.getSelectedItemPosition();
        Course selectedCourse = courseObjects.get(selectedCoursePosition);

        int startHour = Integer.valueOf(startTimeHourSpin.getSelectedItem().toString());
        int endHour = Integer.valueOf(endTimeHourSpin.getSelectedItem().toString());


        if (startAmPm.getSelectedItem().toString().equals("PM")) {
            startHour += 12;
        }
        if (endAmPm.getSelectedItem().toString().equals("PM")) {
            endHour += 12;
        }
        if (startHour >= endHour) {
            Toast.makeText(this, "Please check the start and end time", Toast.LENGTH_SHORT).show();
            dataValid = false;
        } else {
            dataValid = true;
        }


        timeTablePeriod.setCourse(selectedCourse);
        timeTablePeriod.setDayOfWeek(dayOfWeekSpin.getSelectedItem().toString());
        timeTablePeriod.setStartHour(startHour);
        timeTablePeriod.setStartMin(Integer.valueOf(startTimeMinSpin.getSelectedItem().toString()));
        timeTablePeriod.setEndHour(endHour);
        timeTablePeriod.setEndMin(Integer.valueOf(endTimeMinSpin.getSelectedItem().toString()));
        timeTablePeriod.setLecturer(lecturerTxt.getText().toString());
        timeTablePeriod.setPractical(isPracticalCheck.isChecked());

        imeClass.addToTimetable(timeTablePeriod);


    }


    private void populateSpinners() {
        final List<String> listOfCourse = new ArrayList<String>();
        courseObjects = new ArrayList<Course>();
        Integer[] hour = new Integer[12];
        Integer[] min = new Integer[60];

        for (int i = 0; i < hour.length; i++) {
            hour[i] = i + 1;
        }

        for (int i = 0; i < min.length; i++) {
            min[i] = i;
        }

        final ArrayAdapter<Integer> timeHourAdapt = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, hour);
        timeHourAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeHourSpin.setAdapter(timeHourAdapt);
        endTimeHourSpin.setAdapter(timeHourAdapt);

        final ArrayAdapter<Integer> timeMinAdapt = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, min);
        timeHourAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeMinSpin.setAdapter(timeMinAdapt);
        endTimeMinSpin.setAdapter(timeMinAdapt);


        final ArrayAdapter<String> courseSpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listOfCourse);
        courseSpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseSpinnerAdapt);


        firebaseFirestore.collection(DbPaths.Courses.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot result : task.getResult()) {
                        listOfCourse.add(result.getId());
                        courseObjects = task.getResult().toObjects(Course.class);
                    }
                    courseSpinnerAdapt.notifyDataSetChanged();
                }
            }
        });

    }
}
