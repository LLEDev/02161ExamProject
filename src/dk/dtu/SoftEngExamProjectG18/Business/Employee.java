package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;

import java.util.*;

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

    public void assertOpenActivities() throws IllegalArgumentException {
        if (this.getNumOpenActivities() == 0) {
            String output = String.format(
                "The employee %s has no room for any new activities at the moment.",
                this.getID()
            );
            throw new IllegalArgumentException(output);
        }
    }

    public HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> getActivities() {
        return this.activities;
    }

    public ArrayList<Activity> getAllActiveActivities() {
        HashMap<String, Activity> allActivities = new HashMap<>();
        for(HashMap<Integer, EmployeeActivityIntermediate> activities : this.getActivities().values()) {
            for(EmployeeActivityIntermediate intermediate : activities.values()) {
                Activity activity = intermediate.getActivity();
                String combinedID = activity.getProject().getID() + "-" + activity.getID();

                if(!activity.isDone() && !allActivities.containsKey(combinedID)) {
                    allActivities.put(combinedID, activity);
                }
            }
        }

        return new ArrayList<>(allActivities.values());
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
