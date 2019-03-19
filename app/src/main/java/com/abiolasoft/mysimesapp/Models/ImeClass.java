package com.abiolasoft.mysimesapp.Models;

import java.util.ArrayList;
import java.util.List;

public class ImeClass {
    private String classCode;
    private List<TimeTablePeriod> timeTable;

    public ImeClass() {
        if (timeTable == null) {
            timeTable = new ArrayList<TimeTablePeriod>();
        }
    }

    public void addToTimetable(TimeTablePeriod period) {
        timeTable.add(period);
    }

    public void removeFromTimetable(TimeTablePeriod period) {
        timeTable.remove(period);
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<TimeTablePeriod> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(List<TimeTablePeriod> timeTable) {
        this.timeTable = timeTable;
    }
}
