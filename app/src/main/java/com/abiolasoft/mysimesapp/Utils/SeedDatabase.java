package com.abiolasoft.mysimesapp.Utils;

import android.content.Context;

import com.abiolasoft.mysimesapp.Models.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class SeedDatabase {

    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static Context mContext;


    public static void populateCourses(final Context context) {
        mContext = context;

        String nd1_ft = ImeClasses.ND1_FT.name();
        String nd2_ft = ImeClasses.ND2_FT.name();
        String nd1_pt = ImeClasses.ND1_PT.name();
        String nd2_pt = ImeClasses.ND2_PT.name();
        String nd3_pt = ImeClasses.ND3_PT.name();
        String hnd1_ft = ImeClasses.HND1_FT.name();
        String hnd2_ft = ImeClasses.HND2_FT.name();
        String hnd1_pt = ImeClasses.HND1_PT.name();
        String hnd2_pt = ImeClasses.HND2_PT.name();
        String hnd3_pt = ImeClasses.HND3_PT.name();

        final List<String> coursesNames = new ArrayList<String>(
                Arrays.asList(
                        "INTRODUCTION TO COMPUTER",
                        "ELECTRICAL GRAPHICS",
                        "USE OF ENGLISH 1",
                        "CITIZENSHIP EDUCATION 1",
                        "TECHNICAL DRAWING",
                        "BASIC MECHANICAL WORKSHOP PRACTICE",
                        "ALGEBRA AND ELEMENTARY TRIG.",
                        "INTRODUCTION TO STATISTICS",

                        "INTRODUCTION TO ENTREPRENEURSHIP",
                        "ELECTRICAL INSTALLATION",
                        "COMMUNICATION IN ENGLISH I",
                        "CITIZENSHIP EDUCATION",
                        "DESCRIPTIVE DRAWING",
                        "CALCULUS",

                        "ELECTRICAL ENGINEERING SCIENCE",
                        "USE OF ENGLISH II",
                        "MAINTENANCE MANAGEMENT",
                        "INTRODUCTION TO WELDING TECHNOLOGY",
                        "MECHANICAL ENGINEERING SCIENCE",
                        "ENGINEERING DRAWING",

                        "ELECTRONICS I",
                        "ELECTRICAL MEASUREMENT & INSTRUMENTATION",
                        "COMMUNICATION IN ENGLISH II",
                        "MACHINE DRAWING & TECHNICAL SYMBOLS",
                        "METROLOGY",
                        "INTRODUCTION TO THERMODYNAMICS",
                        "MACHINES TOOLS TECHNOLOGY",

                        "STIMULATING ENTREPRENEURSHIP AWARENESS II",
                        "ELECTRIC CIRCUIT THEORY I",
                        "ELECTRICAL POWER",
                        "TECHNICAL REPORT WRITING | PROJECT SEMINAR",
                        "STRUCTURES AND PROPERTIES OF MATERIALS",
                        "FLUID MECHANICS",
                        "LOGIC AND LINEAR ALGEBRA",


                        "COMPUTER SYSTEM & ENGINEERING",
                        "ELECTRICAL MACHINES",
                        "INDUSTRIAL ELECTRONICS",
                        "PROJECT",
                        "PLANT SERVICES & MAINTENANCE",
                        "TRIGONOMETRY & ANALYTICAL GEOMETRY",


                        //hnd1
                        "ENGINEERING IN SOCIETY",
                        "MEASUREMENT & INSTRUMENTATION",
                        "ELECTRO-TECHNIQUES",
                        "SPARE PARTS MANUFACTURE",
                        "COMPUTER APPLICATION & PROGRAMMING",
                        "MECHANICS OF MACHINES",
                        "APPLIED THERMODYNAMICS",
                        "STRENGTH OF MATERIALS",
                        "ADVANCED ALGEBRA",

                        //hnd12
                        "ENTREPRENEURSHIP SKILL DEVELOPMENT I",
                        "COMMUNICATION IN ENGLISH III",
                        "INDUSTRIAL MANAGEMENT",
                        "INDUSTRIAL HYDRAULICS & PNEUMATICS I",
                        "ELECTRIC CIRCUIT THEORY II",
                        "INDUSTRIAL ELECTRONICS II",
                        "ELECT | ELECT SYSTEMS MAINTENANCE I",
                        "MAINTENANCE MANAGEMENT II",
                        "MACHINE DESIGN & CAD",
                        "FLUID MECHANICS",
                        "ADVANCED CALCULUS",

                        //hnd2
                        "ENTREPRENEURSHIP DEVELOPMENT. II",
                        "RESEARCH METHODS | PROJECT SEMINAR",
                        "CONTROL SYSTEMS",
                        "REFRIGERATION & AIR CONDITIONING",
                        "ADVANCED WELDING TECHNOLOGY.",
                        "ELECTRICAL MACHINES II",
                        "INDUSTRIAL SAFETY",
                        "INDUSTRIAL AUTOMATION",
                        "NUMERICAL METHODS",
                        "ELECTRONICS DESIGN & DRAFTING",

                        //hnd22
                        "IND. HYDRAULIC | PNEUMATICS II",
                        "REFRIGERATION & AIR CONDITIONING",
                        "INDUSTRIAL ELECTRONICS III",
                        "ELECT | ELECT SYSTEMS MAINT II",
                        "MAINT. MANAGEMENT III",
                        "MECHANICAL SYSTEMS MAINT",
                        "IND. AUTOMATION II",
                        "PROJECT HND",
                        "STATISTICAL METHODS IN ENGG"


                )
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
                        "STA111",

                        "EED126",
                        "EEG126",
                        "GNS102",
                        "GNS128",
                        "MEC102",
                        "MTH118",

                        "EEC115",
                        "GNS201",
                        "IME201",
                        "IME203",
                        "MEC111",
                        "MEC201",

                        "EEC124",
                        "EEG128",
                        "GNS202",
                        "IME202",
                        "IME204",
                        "MEC108",
                        "MEG110",

                        "EDP216",
                        "EEG231",
                        "EEG245",
                        "IME200",
                        "IME205",
                        "IME207",
                        "MTH211",

                        "EEG242",
                        "EEG246",
                        "EEP244",
                        "IME206",
                        "MEC212",
                        "MTH222",

                        //hnd1
                        "GNS311",
                        "IME301",
                        "IME303",
                        "IME305",
                        "IME307",
                        "MEC315",
                        "MEC321",
                        "MEG317",
                        "MTH311",

                        //hnd12
                        "EED316",
                        "GNS302",
                        "GNS326",
                        "IME302",
                        "IME304",
                        "IME306",
                        "IME308",
                        "IME310",
                        "IME312",
                        "MEG306",
                        "MTH312",

                        //hnd2
                        "EED413",
                        "IME400",
                        "IME401",
                        "IME403",
                        "IME405",
                        "IME407",
                        "IME411",
                        "IME413",
                        "MTH421",
                        "IME409",

                        //hnd22
                        "IME402",
                        "IME404",
                        "IME406",
                        "IME408",
                        "IME410",
                        "IME412",
                        "IME414",
                        "IME416",
                        "STA402"

                )
        );
        List<Integer> courseUnits = new ArrayList<Integer>(
                Arrays.asList(2, 2, 2, 2, 2, 1, 3, 2,
                        2, 2, 2, 2, 2, 2,
                        3, 2, 1, 2, 3, 2,
                        3, 3, 2, 3, 1, 2, 3,
                        2, 3, 3, 1, 2, 3, 2,
                        2, 3, 3, 2, 3, 2,

                        //hnd1
                        2, 3, 2, 3, 3, 3, 3, 3, 3,
                        //hnd12
                        2, 2, 2, 3, 2, 3, 2, 2, 3, 3, 3,
                        //hnd2
                        2, 2, 3, 3, 2, 3, 2, 3, 2, 2,
                        //hnd22
                        3, 2, 3, 2, 2, 3, 3, 3, 2
                )
        );

        List<String> courseClass1 = new ArrayList<String>(
                Arrays.asList(
                        nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt,
                        nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt,
                        nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt,
                        nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt, nd2_pt,
                        nd3_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt, nd1_pt,
                        nd3_pt, nd3_pt, nd3_pt, nd3_pt, nd3_pt, nd3_pt, nd3_pt,

                        //hnd1
                        hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft,

                        //hnd12
                        hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft, hnd1_ft,

                        //hnd2
                        hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft,

                        //hnd22
                        hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft, hnd2_ft

                )
        );

        /*List<String> courseClass2 = new ArrayList<String>(
                Arrays.asList(21, 21, 21, 21, 21, 21, 21, 21)
        );*/

        for (int i = 0; i < coursesNames.size(); i++) {
            Course course = new Course();
            course.setCourseCode(courseCodes.get(i));
            course.setCourseName(coursesNames.get(i));
            course.setCourseUnit(courseUnits.get(i));
            course.setLevels(0, courseClass1.get(i));

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
