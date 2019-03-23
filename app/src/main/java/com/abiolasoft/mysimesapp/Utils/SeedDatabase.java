package com.abiolasoft.mysimesapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abiolasoft.mysimesapp.Models.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeedDatabase {

    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static Context mContext;


    public static void populateCourses(final Context context) {
        mContext = context;
        final List<String> coursesNames = new ArrayList<String>(
                Arrays.asList(
                        "INTRODUCTION TO COMPUTER",
                        "ELECTRICAL GRAPHICS",
                        "USE OF ENGLISH 1",
                        "CITIZENSHIP EDUCATION 1",
                        "TECHNICAL DRAWING",
                        "BASIC MECHANICAL WORKSHOP PRACTICE",
                        "ALGEBRA AND ELEMENTARY TRIG.",
                        "INTRODUCTION TO STATISTICS")
        );
        List<String> courseCodes = new ArrayList<String>(
                Arrays.asList(
                        "COM101",
                        "EEC111",
                        "GNS101",
                        "GNS111",
                        "MEC112",
                        "MEC113",
                        "MTH112",
                        "STA111")
        );
        List<Integer> courseUnits = new ArrayList<Integer>(
                Arrays.asList(2, 2, 2, 2, 2, 1, 3, 2)
        );
        List<Integer> levels = new ArrayList<Integer>(
                Arrays.asList(21, 21, 21, 21, 21, 21, 21, 21)
        );

        for (int i = 0; i < coursesNames.size(); i++) {
            Course course = new Course();
            course.setCourseCode(courseCodes.get(i));
            course.setCourseName(coursesNames.get(i));
            course.setCourseUnit(courseUnits.get(i));
            course.setLevels(new ArrayList<Integer>(Arrays.asList(levels.get(i))));

            final int finalI = i;
            firebaseFirestore.collection(DbPaths.Courses.toString()).document(coursesNames.get(i)).set(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(context, coursesNames.get(finalI) + " added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
