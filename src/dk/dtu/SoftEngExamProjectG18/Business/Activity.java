package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractable;
import dk.dtu.SoftEngExamProjectG18.General.DateFormatter;

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
    protected int estimatedHours = 0;

    protected boolean isDone = false;

    // EmployeeID --> EmployeeActivityIntermediate
    protected HashMap<String, EmployeeActivityIntermediate> trackedTime = new HashMap<>();

    protected void assertStartEndValid(Date start, Date end) {
        if(start == null || end == null) {
            return;
        }

        if(start.compareTo(end) >= 0) {
            String output = String.format(
                "The given start week, %s, is after the given end week, %s.",
                DateFormatter.formatDate(start),
                DateFormatter.formatDate(end)
            );
            throw new IllegalArgumentException(output);
        }
    }

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

    public int getEstimatedHours() {
        return this.estimatedHours;
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

    public int getTotalTrackedMinutes() {
        return this.getTrackedTime().values().stream().mapToInt(EmployeeActivityIntermediate::getTotalMinutes).sum();
    }

    public HashMap<String, EmployeeActivityIntermediate> getTrackedTime() {
        return this.trackedTime;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setEndWeek(Date endWeek) {
        this.assertStartEndValid(this.startWeek, endWeek);
        this.endWeek = endWeek;
    }

    public void setEstimatedHours(int estimatedHours) throws IllegalArgumentException {
        if(estimatedHours <= 0) {
            String output = String.format("The estimated number of work hours has to be bigger than 0. %s received.", estimatedHours);
            throw new IllegalArgumentException(output);
        }

        this.estimatedHours = estimatedHours;
    }

    public void setStartWeek(Date startWeek) {
        this.assertStartEndValid(startWeek, this.endWeek);
        this.startWeek = startWeek;
    }

    /*
        Table extraction methods
     */

    @Override
    public ArrayList<HashMap<String, String>> extract(String context, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection) {
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

            int trackedHours = (int) Math.ceil(activity.getTotalTrackedMinutes() / 60.0);

            HashMap<String, String> entry = new HashMap<>();
            entry.put("ID", String.valueOf(activity.getID()));
            entry.put("Name", activity.getName());
            entry.put("Start week", startWeek);
            entry.put("End week", endWeek);
            entry.put("Estimated work hours (in total)", String.valueOf(activity.getEstimatedHours()));
            entry.put("Tracked work hours (in total)", String.valueOf(trackedHours));

            result.add(entry);
        }

        return result;
    }
}
