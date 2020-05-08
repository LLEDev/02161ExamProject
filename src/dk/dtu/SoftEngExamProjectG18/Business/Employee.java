package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;

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

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Employee(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Employee(String ID, String name, int weeklyActivityCap) {
        this.ID = ID;
        this.name = name;
        this.weeklyActivityCap = weeklyActivityCap;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void addActivity(Activity activity) {
        String projectID = activity.getProject().getID();
        int activityID = activity.getID();

        if (!this.activities.containsKey(projectID)) {
            this.activities.put(projectID, new HashMap<>());
        }

        HashMap<Integer, EmployeeActivityIntermediate> activityMap = this.activities.get(projectID);

        if (!activityMap.containsKey(activityID)) {
            activityMap.put(activityID, EmployeeActivityIntermediate.initAssociation(this, activity));
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void addOOOActivity(OOOActivityType type, Date start, Date end) throws IllegalArgumentException {
        this.OOOActivities.add(new OutOfOfficeActivity(type, start, end));
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void assertOpenActivities() throws IllegalArgumentException {
        if (this.getNumOpenActivities() == 0) {
            String output = String.format(
                "The employee %s has no room for any new activities at the moment.",
                this.getID()
            );
            throw new IllegalArgumentException(output);
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> getActivities() {
        return this.activities;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ArrayList<Activity> getAllActiveActivities() {
        HashMap<String, Activity> allActivities = new HashMap<>();
        for (HashMap<Integer, EmployeeActivityIntermediate> activities : this.getActivities().values()) {
            for (EmployeeActivityIntermediate intermediate : activities.values()) {
                Activity activity = intermediate.getActivity();
                String combinedID = activity.getProject().getID() + "-" + activity.getID();

                if (!activity.isDone() && !allActivities.containsKey(combinedID)) {
                    allActivities.put(combinedID, activity);
                }
            }
        }

        return new ArrayList<>(allActivities.values());
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getID() {
        return this.ID;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getName() {
        return this.name;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public int getNumOpenActivities() {
        return this.weeklyActivityCap - this.activities.size();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ArrayList<OutOfOfficeActivity> getOOOActivities() {
        return this.OOOActivities;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public int getWeeklyActivityCap() {
        return this.weeklyActivityCap;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }
}
