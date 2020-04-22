package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Activity {

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

}
