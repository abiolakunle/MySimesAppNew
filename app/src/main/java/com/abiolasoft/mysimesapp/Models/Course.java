package com.abiolasoft.mysimesapp.Models;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseCode;
    private String courseName;
    private int courseUnit;
    private List<Integer> levels;

    public Course() {
        if (levels == null) {
            levels = new ArrayList<Integer>();
        }
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }


    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(int courseUnit) {
        this.courseUnit = courseUnit;
    }



}
