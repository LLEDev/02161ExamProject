package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Employee {

    protected String ID;
    protected String name;
    protected int weeklyActivityCap = 10;

    // ProjectID --> ActivityID --> EmployeeActivityIntermediate
    protected HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> activities = new HashMap<>();

    protected ArrayList<OutOfOfficeActivity> OOOactivities = new ArrayList<>();

    public Employee(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Employee(String ID, String name, int weeklyActivityCap) {
        this.ID = ID;
        this.name = name;
        this.weeklyActivityCap = weeklyActivityCap;
    }

    public HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> getActivities() {
        return this.activities;
    }

    public int amountOfOpenActivities() {
        return weeklyActivityCap - this.activities.size();
    }

    public String getID() {
        return ID;
    }

    public ArrayList<OutOfOfficeActivity> getOOOactivities() {
        return this.OOOactivities;
    }

    public String getName() {
        return this.name;
    }

    public int getWeeklyActivityCap() {
        return weeklyActivityCap;
    }

    public boolean isOutOfOffie(Date date){
        if (OOOactivities.isEmpty()) {
            return false;
        }

        for (OutOfOfficeActivity current: OOOactivities) {
            if (current.start.compareTo(date) < 0) {
                if (!current.isDone) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }

}
