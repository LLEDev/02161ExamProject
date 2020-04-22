package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Interface.Extractable;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Activity implements Extractable<Activity> {

    protected int ID;
    protected String name;
    protected Project project;

    protected Date endWeek = null;
    protected Date startWeek = null;
    protected int estimatedWeeks = 0;

    protected boolean isDone = false;

    // EmployeeID --> EmployeeActivityIntermediate
    protected HashMap<String, EmployeeActivityIntermediate> trackedTime = new HashMap<>();

    public Activity(String name, Project project) {
        this.ID = project.incrementNextActivityID();
        this.name = name;
        this.project = project;

        project.getActivities().put(this.ID, this);
    }

    public boolean isDone() {
        return this.isDone;
    }

    public Date getEndWeek() {
        return this.endWeek;
    }

    public int getEstimatedWeeks() {
        return estimatedWeeks;
    }

    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public Project getProject() {
        return this.project;
    }

    public Date getStartWeek() {
        return this.startWeek;
    }

    public HashMap<String, EmployeeActivityIntermediate> getTrackedTime() {
        return this.trackedTime;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setEndWeek(Date endWeek) {
        this.endWeek = endWeek;
    }

    public void setEstimatedWeeks(int estimatedWeeks) {
        this.estimatedWeeks = estimatedWeeks;
    }

    public void setStartWeek(Date startWeek) {
        this.startWeek = startWeek;
    }

    /*
        Table extraction methods
     */

    @Override
    public ArrayList<HashMap<String, String>> extract(String context, ArrayList<? extends Extractable<?>> collection) {
        if(context.equals("overview")) {
            return this.extractOverview(collection);
        }

        return null;
    }

    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<? extends Extractable<?>> collection) {
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for(Extractable<?> extractable : collection) {
            if(!(extractable instanceof Activity)) {
                continue;
            }

            Activity activity = (Activity) extractable;

            String startWeek = null;
            String endWeek = null;
            try {
                startWeek = weekFormatter.format(activity.getStartWeek());
                endWeek = weekFormatter.format(activity.getEndWeek());
            } catch (Exception ignored) {}

            HashMap<String, String> entry = new HashMap<>();
            entry.put("ID", String.valueOf(activity.getID()));
            entry.put("Name", activity.getName());
            entry.put("Start week", startWeek);
            entry.put("End week", endWeek);
            entry.put("Estimated duration", activity.getEstimatedWeeks() + " weeks");

            result.add(entry);
        }

        return result;
    }
}
