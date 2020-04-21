package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Employee {

    // ProjectID --> ActivityID --> EmployeeActivityIntermediate
    protected HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> activities = new HashMap<>();
    protected String ID;
    protected String name;
    protected ArrayList<OutOfOfficeActivity> OOOActivities = new ArrayList<>();
    protected int weeklyActivityCap = 10;

    public Employee(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Employee(String ID, String name, int weeklyActivityCap) {
        this.ID = ID;
        this.name = name;
        this.weeklyActivityCap = weeklyActivityCap;
    }

    public void addOOOActivity(OOOActivityType type, Date start, Date end) {
        this.OOOActivities.add(new OutOfOfficeActivity(type, start, end));
    }

    public boolean isOutOfOffice(Date date) {
        for (OutOfOfficeActivity OOOActivity : this.OOOActivities) {
            if (OOOActivity.start.compareTo(date) > 0 || OOOActivity.end.compareTo(date) < 0) {
                continue;
            }

            return true;
        }

        return false;
    }

    public HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> getActivities() {
        return this.activities;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getNumOpenActivities() {
        return this.weeklyActivityCap - this.activities.size();
    }

    public ArrayList<OutOfOfficeActivity> getOOOActivities() {
        return this.OOOActivities;
    }

    public int getWeeklyActivityCap() {
        return this.weeklyActivityCap;
    }

    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }

}
