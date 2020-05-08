package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.Date;
import java.util.HashMap;

public class Activity {

    protected int ID;
    protected String name;
    protected Project project;

    protected Date endWeek = null;
    protected Date startWeek = null;
    protected int estimatedHours = 0;

    protected boolean isDone = false;

    // EmployeeID --> EmployeeActivityIntermediate
    protected HashMap<String, EmployeeActivityIntermediate> trackedTime = new HashMap<>();

    /**
     * @author Someone
     */
    public Activity(String name, Project project) {
        this.ID = project.incrementNextActivityID();
        this.name = name;
        this.project = project;

        project.getActivities().put(this.ID, this);
    }

    /**
     * @author Someone
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * @author Someone
     */
    public Date getEndWeek() {
        return this.endWeek;
    }

    /**
     * @author Someone
     */
    public int getEstimatedHours() {
        return this.estimatedHours;
    }

    /**
     * @author Someone
     */
    public int getID() {
        return this.ID;
    }

    /**
     * @author Someone
     */
    public String getName() {
        return this.name;
    }

    /**
     * @author Someone
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * @author Someone
     */
    public Date getStartWeek() {
        return this.startWeek;
    }

    /**
     * @author Someone
     */
    public int getTotalTrackedMinutes() {
        return this.getTrackedTime().values().stream().mapToInt(EmployeeActivityIntermediate::getTotalMinutes).sum();
    }

    /**
     * @author Someone
     */
    public HashMap<String, EmployeeActivityIntermediate> getTrackedTime() {
        return this.trackedTime;
    }

    /**
     * @author Someone
     */
    public void setDone(boolean done) {
        this.isDone = done;
    }

    /**
     * @author Someone
     */
    public void setEndWeek(Date endWeek) throws IllegalArgumentException {
        Dates.assertStartEndValid(this.startWeek, endWeek);

        this.endWeek = endWeek;
    }

    /**
     * @author Someone
     */
    public void setEstimatedHours(int estimatedHours) throws IllegalArgumentException {
        if (estimatedHours <= 0) {
            String output = String.format("The estimated number of work hours has to be bigger than 0. %s received.", estimatedHours);
            throw new IllegalArgumentException(output);
        }

        this.estimatedHours = estimatedHours;
    }

    /**
     * @author Someone
     */
    public void setStartWeek(Date startWeek) throws IllegalArgumentException {
        Dates.assertStartEndValid(startWeek, this.endWeek);

        this.startWeek = startWeek;
    }
}
