package com.abiolasoft.mysimesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Adapters.TimetableAdapter;
import com.abiolasoft.mysimesapp.Adapters.TimetableDayAdapter;
import com.abiolasoft.mysimesapp.Models.Course;
import com.abiolasoft.mysimesapp.Models.ImeClass;
import com.abiolasoft.mysimesapp.Models.TimeTablePeriod;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.ImeClassSharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

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
    private Bundle intentExtras;
    private ImeClass classToEdit;
    private int positionToEdit;
    private int editOrInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_period);

        firebaseFirestore = FirebaseFirestore.getInstance();

        courseSpinner = findViewById(R.id.timetable_course_spinner);
        dayOfWeekSpin = findViewById(R.id.day_of_week_spin);
        dayOfWeekSpin.setEnabled(false);
        updateBtn = findViewById(R.id.update_timetable_btn);
        startTimeHourSpin = findViewById(R.id.start_hour);
        startTimeMinSpin = findViewById(R.id.start_min);
        endTimeHourSpin = findViewById(R.id.end_hour);
        endTimeMinSpin = findViewById(R.id.end_min);
        lecturerTxt = findViewById(R.id.lecturer_name);
        isPracticalCheck = findViewById(R.id.is_practical_check);
        startAmPm = findViewById(R.id.start_am_pm);
        endAmPm = findViewById(R.id.end_am_pm);


        imeClass = new ImeClass();

        intentExtras = getIntent().getExtras();

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

        firebaseFirestore.collection(DbPaths.Classes.toString()).document(CurrentUserRepo.getOffline().getLevel()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    imeClass.addToTimetable(timeTablePeriod);
                    if (task.getResult().exists()) {
                        if (intentExtras == null) {
                            imeClass = task.getResult().toObject(ImeClass.class);
                            imeClass.addToTimetable(timeTablePeriod);
                        } else {
                            if (editOrInsert == 0) {
                                classToEdit.getTimeTable().set(positionToEdit, timeTablePeriod);
                            } else {
                                classToEdit.getTimeTable().add(positionToEdit, timeTablePeriod);
                            }


                            imeClass = classToEdit;
                        }

                    }

                    firebaseFirestore.collection(DbPaths.Classes.toString()).document(CurrentUserRepo.getOffline().getLevel()).set(imeClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdatePeriodActivity.this, "Class timetable Updated", Toast.LENGTH_SHORT).show();
                                Intent timetableIntent = new Intent(UpdatePeriodActivity.this, TimeTableActivity.class);
                                timetableIntent.putExtra(TimetableDayAdapter.DAY_KEY, timeTablePeriod.getDayOfWeek());
                                startActivity(timetableIntent);
                                finish();
                            }
                        }
                    });
                }
            }
        });


    }

    private void getDataFromViews() {

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

                    if (intentExtras != null) {
                        classToEdit = new ImeClassSharedPref().getObj(TimetableAdapter.CLASS_CODE);
                        positionToEdit = intentExtras.getInt(TimetableAdapter.POSITION_KEY);
                        editOrInsert = intentExtras.getInt(TimetableAdapter.UPDATE_KEY);

                        String dayOfWeekExtra = intentExtras.getString(TimetableDayAdapter.DAY_KEY);
                        if (dayOfWeekExtra != null) {
                            ArrayAdapter dayWeekAdapt = (ArrayAdapter) dayOfWeekSpin.getAdapter();
                            int dayOfWeekPos = dayWeekAdapt.getPosition(dayOfWeekExtra);
                            dayOfWeekSpin.setSelection(dayOfWeekPos);
                        }


                        if (editOrInsert == 0) {

                            int spinPosition = courseSpinnerAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getCourse().getCourseName());
                            courseSpinner.setSelection(spinPosition);

                            int startTimeHourPos = timeHourAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getStartHour());
                            startTimeHourSpin.setSelection(startTimeHourPos);

                            int startTimeMinPos = timeMinAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getStartMin());
                            startTimeMinSpin.setSelection(startTimeMinPos);

                            int endTimeHourPos = timeHourAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getEndHour());
                            endTimeHourSpin.setSelection(endTimeHourPos);

                            int endTimeMinPos = timeMinAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getEndMin());
                            endTimeMinSpin.setSelection(endTimeMinPos);

                            lecturerTxt.setText(classToEdit.getTimeTable().get(positionToEdit).getLecturer());

                            ArrayAdapter dayWeekAdapt = (ArrayAdapter) dayOfWeekSpin.getAdapter();
                            int dayOfWeekPos = dayWeekAdapt.getPosition(classToEdit.getTimeTable().get(positionToEdit).getDayOfWeek());
                            dayOfWeekSpin.setSelection(dayOfWeekPos);

                            isPracticalCheck.setChecked(classToEdit.getTimeTable().get(positionToEdit).isPractical());

                        }

                    } else {
                        imeClass = new ImeClass();
                        imeClass.setClassCode(CurrentUserRepo.getOffline().getLevel());
                    }
                }
            }
        });

    }
}
