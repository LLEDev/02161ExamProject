package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.HashMap;

public class Employee {

    protected String ID;
    protected String name;
    protected int weeklyActivityCap = 10;

    protected HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> activities = new HashMap<>();

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
        int amountOfActivities = this.activities.size();
        return weeklyActivityCap - amountOfActivities;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return this.name;
    }

    public int getWeeklyActivityCap() {
        return weeklyActivityCap;
    }

    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }

}
