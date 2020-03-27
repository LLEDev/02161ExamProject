package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Activity {

    protected int ID;
    protected String name;
    protected Project project;

    protected boolean isDone = false;

    protected Date endWeek = null;
    protected Date startWeek = null;

    protected HashMap<String, EmployeeActivityIntermediate> trackedTime = new HashMap<>();

    public Activity(String name, Project project) {
        this.ID = project.incrementNextActivityID();
        this.name = name;
        this.project = project;

        project.getActivities().put(this.ID, this);
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public boolean isDone() {
        return isDone;
    }

    public Date getEndWeek() {
        return endWeek;
    }

    public Date getStartWeek() {
        return startWeek;
    }

    public HashMap<String, EmployeeActivityIntermediate> getTrackedTime() {
        return this.trackedTime;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setEndWeek(Date endWeek) {
        this.endWeek = endWeek;
    }

    public void setStartWeek(Date startWeek) {
        this.startWeek = startWeek;
    }

}
